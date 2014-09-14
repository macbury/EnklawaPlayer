package macbury.enklawa.services;

import android.app.Notification;
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
import macbury.enklawa.managers.download.DownloadEpisode;
import macbury.enklawa.managers.download.DownloadManager;
import macbury.enklawa.managers.download.DownloadManagerListener;

public class DownloadService extends Service implements DownloadManagerListener {
  private static final String TAG             = "DownloadService";
  private static final String WAKE_LOCK_TAG   = "DownloadService";
  private static final int NOTIFICATION_ID    = 123;
  private PowerManager powerManager;
  private PowerManager.WakeLock wakeLock;
  private ApplicationManager app;
  private static DownloadManager downloadManager;

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
    this.downloadManager = new DownloadManager(this, this);
    this.wakeLock.acquire();
    app.db.episodeFiles.markDownloadingAsFailed();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    wakeLock.release();
    downloadManager.cancelAll();
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    if (app.intents.haveCancelExtra(intent)) {
      if (app.intents.haveEpisode(intent)) {
        Log.i(TAG, "Have episode id in intent, Canceling it!");
        downloadManager.cancelEpisodeById(app.intents.getEpisodeId(intent));
      } else {
        Log.i(TAG, "Cancel all downloads!");
        downloadManager.cancelAll();
      }
    } else {
      Log.i(TAG, "Pushing all pending episodes");
      downloadManager.push(new ArrayList<EpisodeFile>(app.db.episodeFiles.pending()));
    }
    return super.onStartCommand(intent, flags, startId);
  }


  private void sendStatusFor(DownloadEpisode download) {
    sendBroadcast(app.intents.downloadStatus(download));
  }

  private void updateNotification(DownloadEpisode download) {
    Notification notification = app.notifications.downloadEpisode(download.getEpisodeFile().episode, download.progress, downloadManager.size());
    startForeground(NOTIFICATION_ID, notification);
  }

  @Override
  public void onDownloadStart(DownloadEpisode download) {
    updateNotification(download);
    sendStatusFor(download);
  }

  @Override
  public void onDownloadProgress(DownloadEpisode download) {
    updateNotification(download);
    sendStatusFor(download);
  }

  @Override
  public void onDownloadFail(DownloadEpisode download, Exception e) {
    updateNotification(download);
    sendStatusFor(download);
  }

  @Override
  public void onDownloadSuccess(DownloadEpisode download) {
    updateNotification(download);
    sendStatusFor(download);
  }


  @Override
  public void onDownloadManagerFinishedAll() {
    stopSelf();
  }

}
