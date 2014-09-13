package macbury.enklawa.services.download;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.CancellationException;

import macbury.enklawa.db.models.EpisodeFile;
import macbury.enklawa.managers.ApplicationManager;

public class DownloadService extends Service implements ProgressCallback, FutureCallback<File> {
  private static final String TAG             = "DownloadService";
  private static final String WAKE_LOCK_TAG   = "DownloadService";
  private static final int NOTIFICATION_ID    = 123;
  private ArrayList<EpisodeFile> pendingEpisodes;
  public static EpisodeFile currentEpisodeFile;
  public static int progress;
  private PowerManager powerManager;
  private PowerManager.WakeLock wakeLock;
  private ApplicationManager app;
  private Future<File> currentDownload;

  public DownloadService() {
  }

  @Override
  public IBinder onBind(Intent intent) {
    // TODO: Return the communication channel to the service.
    throw new UnsupportedOperationException("Not yet implemented");
  }

  @Override
  public void onCreate() {
    super.onCreate();
    this.app             = ApplicationManager.current();
    this.powerManager    = (PowerManager) getSystemService(POWER_SERVICE);
    this.wakeLock        = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, WAKE_LOCK_TAG);
    this.pendingEpisodes = new ArrayList();
    this.wakeLock.acquire();

    app.db.episodeFiles.markDownloadingAsFailed();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    wakeLock.release();
    Ion.getDefault(this).cancelAll(this);
    currentEpisodeFile = null;
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    if (app.intents.haveCancelExtra(intent)) {
      Log.i(TAG, "Recived cancel task intent action");
      if (app.intents.haveEpisode(intent)) {
        cancelEpisodeById(app.intents.getEpisodeId(intent));
      } else {
        Log.i(TAG, "Cancel all downloads!");
        finish();
      }

    } else {
      pendingEpisodes.clear();
      pendingEpisodes.addAll(ApplicationManager.current().db.episodeFiles.pending());
      nextUnlessRunning();
    }
    return super.onStartCommand(intent, flags, startId);
  }

  private void cancelEpisodeById(int episodeId) {
    if (currentEpisodeFile != null && currentEpisodeFile.episode.id == episodeId) {
      Log.d(TAG, "Canceling current task!");
      if (currentDownload != null)
        currentDownload.cancel(true);
      currentDownload = null;
    } else {
      Log.d(TAG, "Finding task in queue");
      EpisodeFile toRemove = null;
      for(EpisodeFile file : pendingEpisodes) {
        if (file.episode.id == episodeId) {
          toRemove = file;
        }
      }

      if (toRemove != null) {
        pendingEpisodes.remove(toRemove);
      }
    }
  }

  private void nextUnlessRunning() {
    if (currentEpisodeFile == null) {
      next();
    }
  }


  private void finish() {
    if (currentEpisodeFile != null) {
      currentEpisodeFile.fail();
      ApplicationManager.current().db.episodeFiles.update(currentEpisodeFile);
      status(0);
    }
    stopSelf();
  }

  private void next() {
    if (this.pendingEpisodes.size() > 0) {
      currentEpisodeFile = this.pendingEpisodes.remove(0);
      currentEpisodeFile.running();
      ApplicationManager.current().db.episodeFiles.update(currentEpisodeFile);

      status(0);
      Log.i(TAG, "Downloading: " + currentEpisodeFile.episode.name);

      currentDownload = Ion.with(this).load(currentEpisodeFile.episode.mp3).progress(this).write(currentEpisodeFile.file(this)).setCallback(this);
    } else {
      afterDownloadAll();
    }
  }

  @Override
  public void onProgress(long downloaded, long total) {
    progress = (int) (((float)downloaded/(float)total) * 100);

    if (progress % 5 == 0) {
      status(progress);
    }
  }

  private void status(int progress) {
    sendBroadcast(ApplicationManager.current().intents.downloadStatus(currentEpisodeFile, progress));
    startForeground(NOTIFICATION_ID, ApplicationManager.current().notifications.downloadEpisode(currentEpisodeFile.episode, progress, pendingEpisodes.size()));
  }

  @Override
  public void onCompleted(Exception e, File result) {
    if (e != null) {
      if (CancellationException.class.isInstance(e)) {
        Log.e(TAG, "Forced cancel");
        app.db.episodeFiles.destroy(currentEpisodeFile);
      } else {
        Log.e(TAG, "Something failed:" +e.toString());
        currentEpisodeFile.fail();
        app.db.episodeFiles.update(currentEpisodeFile);
      }

    } else {
      Log.i(TAG, "Downloaded successul!");
      currentEpisodeFile.success();
      app.db.episodeFiles.update(currentEpisodeFile);
    }

    status(100);
    currentEpisodeFile = null;
    currentDownload    = null;
    next();
  }

  private void afterDownloadAll() {
    Log.i(TAG, "After download all!");
    currentEpisodeFile = null;
    currentDownload    = null;
    finish();
  }

  public boolean isRunning() {
    return currentEpisodeFile != null;
  }
}
