package macbury.enklawa.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;

import macbury.enklawa.managers.Enklawa;

public class MediaButtonReceiver extends BroadcastReceiver {
  private static final String TAG = "MediaButtonReceiver";

  public MediaButtonReceiver() {
  }

  @Override
  public void onReceive(Context context, Intent intent) {
    KeyEvent event = (KeyEvent) intent.getExtras().get(Intent.EXTRA_KEY_EVENT);
    if (event.getAction() == KeyEvent.ACTION_DOWN) {
      int keycode = event.getKeyCode();
      context.startService(Enklawa.current().intents.playerHandleRemoteControlClient(keycode));
      Log.d(TAG, "Recived keycode: " + keycode);
    }
  }
}
