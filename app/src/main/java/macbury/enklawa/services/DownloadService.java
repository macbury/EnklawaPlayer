package macbury.enklawa.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import macbury.enklawa.db.models.EpisodeFile;
import macbury.enklawa.managers.ApplicationManager;

public class DownloadService extends Service implements ProgressCallback, FutureCallback<File> {
  private static final String TAG = "DownloadService";
  private static final String WAKE_LOCK_TAG = "DownloadService";
  private static final int NOTIFICATION_ID = 123;
  private Stack<EpisodeFile> pendingEpisodes;
  private EpisodeFile currentEpisodeFile;
  private PowerManager powerManager;
  private PowerManager.WakeLock wakeLock;

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
    this.powerManager    = (PowerManager) getSystemService(POWER_SERVICE);
    this.wakeLock        = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, WAKE_LOCK_TAG);
    this.pendingEpisodes = new Stack();
    pendingEpisodes.addAll(ApplicationManager.current().db.episodeFiles.pending());
    this.wakeLock.acquire();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    wakeLock.release();
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    next();
    return super.onStartCommand(intent, flags, startId);
  }

  private void next() {
    if (this.pendingEpisodes.size() > 0) {
      currentEpisodeFile = this.pendingEpisodes.pop();
      Log.i(TAG, "Downloading: " + currentEpisodeFile.episode.name);
      Ion.with(this).load(currentEpisodeFile.episode.mp3).progress(this).write(currentEpisodeFile.file(this)).setCallback(this);
    } else {
      afterDownloadAll();
    }
  }

  @Override
  public void onProgress(long downloaded, long total) {
    int progress = (int) (((float)downloaded/(float)total) * 100);
    startForeground(NOTIFICATION_ID, ApplicationManager.current().notifications.downloadEpisode(currentEpisodeFile.episode, progress, pendingEpisodes.size()));
  }

  @Override
  public void onCompleted(Exception e, File result) {
    if (e != null) {
      Log.e(TAG, e.toString());
      currentEpisodeFile.fail();
    } else {
      Log.i(TAG, "Downloaded successul!");
      currentEpisodeFile.success();
    }

    ApplicationManager.current().db.episodeFiles.update(currentEpisodeFile);
    next();
  }

  private void afterDownloadAll() {
    Log.i(TAG, "After download all!");
    stopSelf();
  }
}
