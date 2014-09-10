package macbury.enklawa.managers;

import android.app.Notification;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import macbury.enklawa.R;

/**
 * Created by macbury on 10.09.14.
 */
public class NotificationsManager {
  private final Context context;

  public NotificationsManager(Context applicationContext) {
    this.context = applicationContext;
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
}
