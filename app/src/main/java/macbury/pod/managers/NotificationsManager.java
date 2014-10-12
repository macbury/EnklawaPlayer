package macbury.pod.managers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat;

import macbury.pod.R;
import macbury.pod.db.models.EnqueueEpisode;
import macbury.pod.db.models.Episode;
import macbury.pod.extensions.Converter;
import macbury.pod.managers.player.PlaybackStatus;

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

    PendingIntent playEpisodePendingIntent = App.current().intents.pendingOpenPlayerForEpisode(episode);
    builder.addAction(R.drawable.ic_action_av_play, context.getString(R.string.notification_play_action), playEpisodePendingIntent);
    builder.setContentIntent(playEpisodePendingIntent);

    Notification notification = builder.build();
    return notification;
  }

  public Notification playEpisode(Bitmap preview, EnqueueEpisode enqueeEpisode) {
    NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
            .setSmallIcon(R.drawable.ic_stat_enklawa_notification)
            .setContentTitle(enqueeEpisode.episode.name)
            .setAutoCancel(false)
            .setOngoing(true)
            .setTicker(enqueeEpisode.episode.name)
            .setContentInfo(Converter.getDurationStringLong(enqueeEpisode.episode.duration))
            .setContentText(enqueeEpisode.episode.program.name);

    if (preview != null) {
      builder.setLargeIcon(preview);
    }

    PendingIntent openPlayerIntent = App.current().intents.pendingOpenPlayerForEpisode(enqueeEpisode.episode);

    if (enqueeEpisode.status != PlaybackStatus.Playing) {
      builder.addAction(R.drawable.ic_action_av_play, context.getString(R.string.notification_play_action), App.current().intents.pendingPlayEpisode(enqueeEpisode.episode));
    } else {
      NotificationCompat.Action pauseAction = new NotificationCompat.Action(R.drawable.ic_action_av_pause, context.getString(R.string.notification_pause_action), App.current().intents.pendingPausePlayer());
      builder.addAction(pauseAction);
    }

    //NotificationCompat.Action stopAction = new NotificationCompat.Action(R.drawable.ic_action_av_stop, context.getString(R.string.notification_stop_action), Enklawa.current().intents.pendingStopPlayer());
    //builder.addAction(stopAction);

    builder.setContentIntent(openPlayerIntent);

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

    builder.addAction(R.drawable.ic_action_av_pause, context.getString(R.string.stop_download), App.current().intents.cancelDownloadService());
    Notification notification = builder.build();
    return notification;
  }
}
