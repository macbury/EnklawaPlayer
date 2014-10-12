package macbury.pod.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import macbury.pod.managers.App;

public class BootReceiver extends BroadcastReceiver {
  public BootReceiver() {
  }

  @Override
  public void onReceive(Context context, Intent intent) {
    App.current().alarms.setup();
  }
}
