package macbury.enklawa.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import macbury.enklawa.managers.Enklawa;
import macbury.enklawa.managers.player.PlayerManager;

public class PlayerService extends Service {
  private Enklawa app;
  private PlayerManager playerManager;

  public PlayerService() {
  }

  @Override
  public void onCreate() {
    super.onCreate();
    this.app           = Enklawa.current();
    this.playerManager = new PlayerManager(getApplicationContext());
  }

  @Override
  public void onDestroy() {
    playerManager.destroy();
    super.onDestroy();
  }

  @Override
  public IBinder onBind(Intent intent) {
    // TODO: Return the communication channel to the service.
    throw new UnsupportedOperationException("Not yet implemented");
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    if (app.intents.haveEpisode(intent)) {

    }
    return super.onStartCommand(intent, flags, startId);
  }
}
