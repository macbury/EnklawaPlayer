package macbury.enklawa.services;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;

import java.util.ArrayList;

import macbury.enklawa.api.APIEpisode;
import macbury.enklawa.api.APIProgram;
import macbury.enklawa.api.APIResponse;
import macbury.enklawa.api.APIThread;
import macbury.enklawa.db.ExternalDBCallbacks;
import macbury.enklawa.db.models.BaseModel;
import macbury.enklawa.db.models.Episode;
import macbury.enklawa.db.models.EpisodeFile;
import macbury.enklawa.db.models.ForumThread;
import macbury.enklawa.db.models.Program;
import macbury.enklawa.db.scopes.EpisodesScope;
import macbury.enklawa.db.scopes.ProgramsScope;
import macbury.enklawa.db.scopes.ThreadScope;
import macbury.enklawa.managers.ApplicationManager;

public class SyncPodService extends Service implements FutureCallback<APIResponse>, ProgressCallback {
  private static final String TAG = "SyncPodService";
  private static final String WAKE_LOCK_TAG = "SyncPodWakeLock";
  private static final int SYNC_POD_NOTIFICATION_ID = 666;
  private ApplicationManager app;
  private PowerManager powerManager;
  private PowerManager.WakeLock wakeLock;

  private static boolean running = false;

  public SyncPodService() {
  }

  public static boolean isRunning() {
    return running;
  }

  @Override
  public IBinder onBind(Intent intent) {
    throw new UnsupportedOperationException("Not yet implemented");
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    Log.d(TAG, "Destroy");
    wakeLock.release();
    Ion.getDefault(this).cancelAll(this);
    running = false;
    app.broadcasts.podSync();
  }

  @Override
  public void onCreate() {
    super.onCreate();
    Log.d(TAG, "Create");
    running           = true;
    this.app          = (ApplicationManager)getApplication();
    this.powerManager = (PowerManager) getSystemService(POWER_SERVICE);
    this.wakeLock     = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, WAKE_LOCK_TAG);
    this.wakeLock.acquire();
    app.broadcasts.podSync();
    syncPod();
  }

  private void progress(int progress){
    startForeground(SYNC_POD_NOTIFICATION_ID, this.app.notifications.syncPod(progress));
  }

  private void syncPod() {
    progress(0);
    Log.i(TAG, "Syncing pod: "+app.settings.getApiEndpoint());
    Ion.with(this).load(app.settings.getApiEndpoint()).progress(this).as(new TypeToken<APIResponse>() {}).setCallback(this);
  }

  @Override
  public void onCompleted(Exception e, APIResponse result) {
    progress(0);

    if (e == null) {
      Log.d(TAG, "Downloaded pod data!");
      SyncApiResponseWithDB syncDB = new SyncApiResponseWithDB();
      syncDB.execute(result);
    } else {
      Log.e(TAG, "Error while downloading pod data!");
      e.printStackTrace();
      showErrorSyncNotification(e);
      stopSelf();
    }
  }

  private void showErrorSyncNotification(Exception e) {
    app.notifications.manager.notify(1, app.notifications.syncPodError(e));
  }

  @Override
  public void onProgress(long downloaded, long total) {
    progress(Math.round(downloaded / total * 100));
  }

  private void showNotificationsForNewEpisodesOrDownloadThem(ArrayList<Episode> newEpisodes) {
    for(Episode episode : newEpisodes) {
      app.db.episodeFiles.createFromEpisode(episode);
      if (episode.program.isFavorite()) {
        Log.i(TAG, "Show notification!!!!");
      }
      Log.i(TAG, "New episode: " + episode.name);
    }
    // else show summary
    // start download service

    for(EpisodeFile file : app.db.episodeFiles.all()) {
      Log.i(TAG, file.toString());
    }
  }

  private class SyncApiResponseWithDB extends AsyncTask<APIResponse, Integer, Boolean> implements ExternalDBCallbacks {
    private ArrayList<Episode> newEpisodes;
    private float current = 0;
    private float total   = 1;

    private void syncProgramsAndEpisodes(ArrayList<APIProgram> resultPrograms) {
      ProgramsScope programs = app.db.programs;
      EpisodesScope episodes = app.db.episodes;
      for (APIProgram apiProgram : resultPrograms) {
        current++;
        if (programs.update(programs.buildFromApi(apiProgram))) {
          Program program = programs.find(apiProgram);
          Log.v(TAG, "Program: " +program.name);
          for(APIEpisode apiEpisode : apiProgram.episodes) {
            Episode episode = episodes.buildFromApi(apiEpisode);
            episode.program = program;
            episode.setListener(this);
            current++;
            publishProgress();
            if (episodes.update(episode)) {
              Log.v(TAG, "Episode: " + episode.name);
            } else {
              Log.e(TAG, "Could not save episode: " + episode.name);
            }
          }
        } else {
          Log.e(TAG, "Could not save program: " + apiProgram.name);
        }
      }
    }

    private void syncThreads(ArrayList<APIThread> resultThreads) {
      ThreadScope threads = app.db.threads;
      for (APIThread apiThread : resultThreads) {
        current++;
        ForumThread thread = threads.find(apiThread);
        if (thread == null) {
          thread = threads.buildFromApi(apiThread);
          thread.setListener(this);
          threads.update(thread);
        }
      }
    }

    @Override
    protected Boolean doInBackground(APIResponse... params) {
      newEpisodes            = new ArrayList<Episode>();
      APIResponse result     = params[0];
      this.total             = result.countProgramsAndEpisodes();

      syncThreads(result.forum);
      syncProgramsAndEpisodes(result.programs);

      return true;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
      super.onProgressUpdate(values);
      SyncPodService.this.progress(Math.round(current / total * 100));
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
      super.onPostExecute(aBoolean);
      SyncPodService.this.showNotificationsForNewEpisodesOrDownloadThem(newEpisodes);
      stopSelf();
    }

    @Override
    public void afterCreate(BaseModel model) {
      if (Episode.class.isInstance(model)) {
        Episode episode = (Episode)model;
        if (episode.isFresh()) {
          newEpisodes.add(episode);
        }
      }

      if (ForumThread.class.isInstance(model)) {
        ForumThread thread = (ForumThread)model;
        Log.i("New thread", thread.title);
      }
    }

    @Override
    public void afterDestroy(BaseModel object) {

    }

    @Override
    public void afterSave(BaseModel object) {

    }
  }

}
