package macbury.enklawa.managers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import macbury.enklawa.R;
import macbury.enklawa.db.models.Episode;
import macbury.enklawa.services.DownloadService;

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

  public Notification syncPod(int progress) {
    NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
            .setSmallIcon(R.drawable.ic_launcher)
            .setContentTitle(context.getString(R.string.notification_sync_pod_title))
            .setContentText(context.getString(R.string.notification_sync_pod_content));

    if (progress == 0) {
      builder.setProgress(100, 0, true);
    } else {
      builder.setProgress(100, progress, false);
    }

    return builder.build();
  }

  public Notification syncPodError(Exception e) {
    NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
            .setSmallIcon(android.R.drawable.stat_sys_download)
            .setContentTitle(context.getString(R.string.notification_sync_pod_error_title))
            .setContentText(e.getLocalizedMessage());

    return builder.build();
  }

  public Notification downloadEpisode(Episode episode, int progress, int left) {
    NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
            .setSmallIcon(android.R.drawable.stat_sys_download)
            .setContentTitle(context.getString(R.string.notification_download_title))
            .setContentInfo("" + left)
            .setContentText(episode.name);

    if (progress == 0) {
      builder.setProgress(100, 0, true);
    } else {
      builder.setProgress(100, progress, false);
    }


    Intent intent         = new Intent(context, DownloadService.class);
    PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);

    builder.addAction(R.drawable.ic_action_av_pause, "Cancel", pIntent);

    return builder.build();
  }
}
