package macbury.enklawa.managers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import macbury.enklawa.db.models.Program;

/**
 * Created by macbury on 11.09.14.
 */
public class BroadcastsManager {
  public static final String BROADCAST_ACTION_SYNCING     = "macbury.enklawa.BROADCAST_ACTION_SYNCING";
  public static final String BROADCAST_ACTION_DOWNLOADING = "macbury.enklawa.BROADCAST_ACTION_DOWNLOADING";
  public static final String BROADCAST_FAVORITE_PROGRAM   = "macbury.enklawa.BROADCAST_FAVORITE_PROGRAM";
  private final Enklawa context;

  public BroadcastsManager(Enklawa enklawa) {
    this.context = enklawa;
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

  public void favoriteProgramChangeReceiver(Context context, BroadcastReceiver receiver) {
    context.registerReceiver(receiver, new IntentFilter(BROADCAST_FAVORITE_PROGRAM));
  }
}
