package macbury.enklawa.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

import macbury.enklawa.db.models.EpisodeFile;
import macbury.enklawa.managers.Enklawa;
import macbury.enklawa.managers.download.DownloadEpisode;
import macbury.enklawa.managers.download.DownloadManager;
import macbury.enklawa.managers.download.DownloadManagerListener;

public class DownloadService extends Service implements DownloadManagerListener {
  private static final String TAG             = "DownloadService";
  private static final String WAKE_LOCK_TAG   = "DownloadService";
  private static final int NOTIFICATION_ID    = 123;
  private static final String WIFI_LOCK_TAG   = "DownloadService";
  private PowerManager powerManager;
  private PowerManager.WakeLock wakeLock;
  private Enklawa app;
  private static DownloadManager downloadManager;
  private NotificationManager mNotificationManager;
  private WifiManager.WifiLock wifiLock;

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
    this.wifiLock             = ((WifiManager) getSystemService(Context.WIFI_SERVICE)) .createWifiLock(WifiManager.WIFI_MODE_FULL, WIFI_LOCK_TAG);
    this.mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    this.app                  = Enklawa.current();
    this.powerManager         = (PowerManager) getSystemService(POWER_SERVICE);
    this.wakeLock             = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, WAKE_LOCK_TAG);
    this.downloadManager      = new DownloadManager(this, this);
    this.wakeLock.acquire();
    this.wifiLock.acquire();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    wakeLock.release();
    wifiLock.release();
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
  public void onDownloadSuccess(final DownloadEpisode download) {
    updateNotification(download);
    sendStatusFor(download);
    Enklawa.current().db.queue.createFromEpisode(download.getEpisode());
    Ion.with(Enklawa.current()).load(download.getEpisode().image).asBitmap().setCallback(new FutureCallback<Bitmap>() {
      @Override
      public void onCompleted(Exception e, Bitmap result) {
        mNotificationManager.notify(download.getEpisodeFileId(), app.notifications.downloadedEpisode(result, download.getEpisode()));
      }
    });
  }

  @Override
  public void onDownloadManagerFinishedAll() {
    stopSelf();
  }

}
