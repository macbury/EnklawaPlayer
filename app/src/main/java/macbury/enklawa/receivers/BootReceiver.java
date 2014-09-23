package macbury.enklawa.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import macbury.enklawa.managers.Enklawa;

public class BootReceiver extends BroadcastReceiver {
  public BootReceiver() {
  }

  @Override
  public void onReceive(Context context, Intent intent) {
    Enklawa.current().alarms.setup();
  }
}
