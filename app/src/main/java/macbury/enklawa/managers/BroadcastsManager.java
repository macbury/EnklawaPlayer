package macbury.enklawa.managers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Created by macbury on 11.09.14.
 */
public class BroadcastsManager {
  private static final String BROADCAST_ACTION__SYNCING = "macbury.enklawa.BROADCAST_ACTION__SYNCING";
  private final ApplicationManager context;

  public BroadcastsManager(ApplicationManager applicationManager) {
    this.context = applicationManager;
  }

  public void podSync() {
    Intent intent = new Intent(BROADCAST_ACTION__SYNCING);
    this.context.sendBroadcast(intent);
  }

  public void podSyncReceiver(Context context, BroadcastReceiver receiver) {
    context.registerReceiver(receiver, new IntentFilter(BROADCAST_ACTION__SYNCING));
  }
}
