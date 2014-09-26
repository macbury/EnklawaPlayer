package macbury.enklawa.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;

import macbury.enklawa.db.models.EnqueueEpisode;
import macbury.enklawa.db.models.Episode;
import macbury.enklawa.managers.Enklawa;
import macbury.enklawa.managers.player.PlayerManager;

public class PlayerService extends Service {
  private static final String TAG = "PlayerService";
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
      Episode episode               = app.db.episodes.find(app.intents.getEpisodeId(intent));
      EnqueueEpisode enqueueEpisode = app.db.queue.createFromEpisode(episode);
      Log.i(TAG, "Recived episode to play:" + episode.name);
      playerManager.add(enqueueEpisode);
    } else {
      Log.i(TAG, "Play all enqeued episodes");
      playerManager.set(new ArrayList<EnqueueEpisode>(app.db.queue.all()));
    }
    return super.onStartCommand(intent, flags, startId);
  }
}
