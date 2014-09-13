package macbury.enklawa.managers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import macbury.enklawa.db.models.EpisodeFile;

/**
 * Created by macbury on 11.09.14.
 */
public class BroadcastsManager {
  private static final String BROADCAST_ACTION_SYNCING     = "macbury.enklawa.BROADCAST_ACTION_SYNCING";
  public static final String BROADCAST_ACTION_DOWNLOADING = "macbury.enklawa.BROADCAST_ACTION_DOWNLOADING";
  private final ApplicationManager context;

  public BroadcastsManager(ApplicationManager applicationManager) {
    this.context = applicationManager;
  }

  public void podSync() {
    Intent intent = new Intent(BROADCAST_ACTION_SYNCING);
    this.context.sendBroadcast(intent);
  }


  public void podSyncReceiver(Context context, BroadcastReceiver receiver) {
    context.registerReceiver(receiver, new IntentFilter(BROADCAST_ACTION_SYNCING));
  }

  public void downloadReceiver(Context context, BroadcastReceiver receiver) {
    context.registerReceiver(receiver, new IntentFilter(BROADCAST_ACTION_DOWNLOADING));
  }
}
