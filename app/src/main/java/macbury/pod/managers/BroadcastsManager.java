package macbury.pod.managers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import macbury.pod.db.models.Program;

/**
 * Created by macbury on 11.09.14.
 */
public class BroadcastsManager {
  public static final String BROADCAST_ACTION_SYNCING     = "macbury.enklawa.BROADCAST_ACTION_SYNCING";
  public static final String BROADCAST_ACTION_DOWNLOADING = "macbury.enklawa.BROADCAST_ACTION_DOWNLOADING";
  public static final String BROADCAST_FAVORITE_PROGRAM   = "macbury.enklawa.BROADCAST_FAVORITE_PROGRAM";
  public static final String BROADCAST_PLAYER_STATUS      = "macbury.enklawa.BROADCAST_PLAYER_STATUS";

  private final App context;

  public BroadcastsManager(App app) {
    this.context = app;
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

  public void favoriteProgramChange(Program program) {
    context.sendBroadcast(context.intents.favoriteProgram(program));
  }

  public void playerStatusChanged() {
    Intent intent = new Intent(BROADCAST_PLAYER_STATUS);
    context.sendBroadcast(intent);
  }

  public void playerStatusChangedReceiver(Context context, BroadcastReceiver receiver) {
    context.registerReceiver(receiver, new IntentFilter(BROADCAST_PLAYER_STATUS));
  }

  public void favoriteProgramChangeReceiver(Context context, BroadcastReceiver receiver) {
    context.registerReceiver(receiver, new IntentFilter(BROADCAST_FAVORITE_PROGRAM));
  }
}
