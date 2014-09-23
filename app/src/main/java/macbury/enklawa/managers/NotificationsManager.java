package macbury.enklawa.managers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat;

import macbury.enklawa.R;
import macbury.enklawa.db.models.Episode;
import macbury.enklawa.extensions.Converter;

/**
 * Created by macbury on 10.09.14.
 */
public class NotificationsManager {
  private final Context context;
  public final NotificationManager manager;

  public NotificationsManager(Context applicationContext) {
    this.context = applicationContext;
    this.manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
  }

  public Notification syncPod() {
    NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
            .setSmallIcon(R.drawable.ic_stat_enklawa_notification)
            .setContentTitle(context.getString(R.string.notification_sync_pod_title))
            .setContentText(context.getString(R.string.notification_sync_pod_content));
    builder.setProgress(100, 0, true);

    return builder.build();
  }

  public Notification syncPodError(Exception e) {
    NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
            .setSmallIcon(R.drawable.ic_stat_enklawa_notification)
            .setContentTitle(context.getString(R.string.notification_sync_pod_error_title))
            .setContentText(e.getLocalizedMessage());

    return builder.build();
  }

  public Notification downloadedEpisode(Bitmap preview, Episode episode) {
    NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
            .setSmallIcon(R.drawable.ic_stat_enklawa_notification)
            .setLargeIcon(preview)
            .setContentTitle(episode.name)
            .setAutoCancel(true)
            .setTicker(episode.name)
            .setContentInfo(Converter.getDurationStringLong(episode.duration))
            .setContentText(episode.program.name);

    PendingIntent playEpisodePendingIntent = Enklawa.current().intents.pendingOpenPlayerForEpisode(episode);
    builder.addAction(R.drawable.ic_action_av_play, context.getString(R.string.notification_play_action), playEpisodePendingIntent);
    builder.setContentIntent(playEpisodePendingIntent);

    Notification notification = builder.build();
    return notification;
  }

  public Notification downloadEpisode(Episode episode, int progress, int left) {
    NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
            .setSmallIcon(android.R.drawable.stat_sys_download)
            .setContentTitle(context.getString(R.string.notification_download_title))
            .setContentInfo(String.valueOf(left))
            .setContentText(episode.name);

    if (progress == 0) {
      builder.setProgress(100, 0, true);
    } else {
      builder.setProgress(100, progress, false);
    }

    builder.addAction(R.drawable.ic_action_av_pause, context.getString(R.string.stop_download), Enklawa.current().intents.cancelDownloadService());
    Notification notification = builder.build();
    return notification;
  }
}
