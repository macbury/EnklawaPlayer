package macbury.enklawa.managers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by macbury on 23.09.14.
 */
public class AlarmsManager {
  private static final String TAG = "AlarmsManager";
  private final Enklawa application;
  private final AlarmManager manager;

  public AlarmsManager(Enklawa application) {
    this.application = application;
    this.manager     = (AlarmManager)application.getSystemService(Context.ALARM_SERVICE);
  }

  public void setup() {
    Log.i(TAG, "Setup alarms");
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(System.currentTimeMillis());
    calendar.set(Calendar.HOUR_OF_DAY, 0);

    PendingIntent syncPodIntent = application.intents.syncPod();
    long syncFreq               = application.settings.getSyncFreq();
    if (syncFreq == -1) {
      Log.i(TAG, "Sync freq is -1, canceling intent");
      manager.cancel(syncPodIntent);
    } else {
      Log.i(TAG, "Configured inexact timer!");
      manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), syncFreq, syncPodIntent);
    }
    manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_HALF_HOUR, application.intents.downloadPendingEpisodes());
  }

}
