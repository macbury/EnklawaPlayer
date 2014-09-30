package macbury.enklawa.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

import macbury.enklawa.db.models.EnqueueEpisode;
import macbury.enklawa.db.models.Episode;
import macbury.enklawa.managers.Enklawa;
import macbury.enklawa.managers.player.PlayerManager;
import macbury.enklawa.managers.player.PlayerManagerListener;
import macbury.enklawa.managers.player.sources.AbstractMediaSource;
import macbury.enklawa.managers.player.sources.EpisodeMediaSource;

public class PlayerService extends Service implements PlayerManagerListener {
  private static final String TAG = "PlayerService";
  private static final int NOTIFICATION_PLAYED_ID = 689;
  private static final String WIFI_LOCK_TAG = "EnklawaPlayerService";
  private Enklawa app;
  private PlayerManager playerManager;
  private final IBinder playerManagerBinder = new PlayerBinder();
  private Bitmap currentBitmapArt;
  private WifiManager.WifiLock wifiLock;
  private static boolean running;

  public PlayerService() {
  }

  @Override
  public void onCreate() {
    super.onCreate();

    this.wifiLock = ((WifiManager) getSystemService(Context.WIFI_SERVICE)) .createWifiLock(WifiManager.WIFI_MODE_FULL, WIFI_LOCK_TAG);

    this.app           = Enklawa.current();
    this.playerManager = new PlayerManager(getApplicationContext());
    playerManager.addListener(this);

    wifiLock.acquire();
    running = true;
    app.broadcasts.playerStatusChanged();

    registerReceiver(headsetDisconnected, new IntentFilter(Intent.ACTION_HEADSET_PLUG));
    registerReceiver(audioBecomingNoisy, new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY));
  }

  @Override
  public void onDestroy() {
    playerManager.removeListener(this);
    playerManager.destroy();
    wifiLock.release();
    running = false;
    unregisterReceiver(headsetDisconnected);
    unregisterReceiver(audioBecomingNoisy);
    super.onDestroy();
  }

  @Override
  public IBinder onBind(Intent intent) {
    return playerManagerBinder;
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    if (app.intents.havePauseExtra(intent)) {
      playerManager.pause();
    } else if (app.intents.haveCancelExtra(intent)) {
      playerManager.cancel();
    } else if (app.intents.haveEpisode(intent)) {
      Episode episode               = app.db.episodes.find(app.intents.getEpisodeId(intent));
      EnqueueEpisode enqueueEpisode = app.db.queue.createFromEpisode(episode);
      Log.i(TAG, "Recived episode to play:" + episode.name);
      if (playerManager.is(enqueueEpisode)) {
        playerManager.play();
      } else {
        app.db.queue.moveToBegining(enqueueEpisode);
        playerManager.restart();
      }

    } else {
      Log.i(TAG, "Play all enqeued episodes");
      playerManager.start();
    }
    return super.onStartCommand(intent, flags, startId);
  }

  @Override
  public void onInitialize(PlayerManager manager, AbstractMediaSource mediaSource) {
    //sendBroadcast();
    Ion.with(this).load(mediaSource.getPreviewArtUri().toString()).asBitmap().setCallback(new FutureCallback<Bitmap>() {
      @Override
      public void onCompleted(Exception e, Bitmap result) {
        PlayerService.this.currentBitmapArt = result;
        updateNotification();
      }
    });
    updateNotification();
  }

  private void updateNotification() {
    EpisodeMediaSource ems = (EpisodeMediaSource) playerManager.getCurrentMediaSource();
    startForeground(NOTIFICATION_PLAYED_ID, Enklawa.current().notifications.playEpisode(currentBitmapArt, ems.getEpisode()));
  }

  @Override
  public void onFinishAll(PlayerManager manager) {
    stopSelf();
  }

  @Override
  public void onPlay(PlayerManager manager, AbstractMediaSource mediaSource) {
    updateNotification();
  }

  @Override
  public void onPause(PlayerManager manager, AbstractMediaSource mediaSource) {
    updateNotification();
  }

  @Override
  public void onFinish(PlayerManager manager, AbstractMediaSource mediaSource) {
    updateNotification();
  }

  @Override
  public void onBufferMedia(PlayerManager manager, AbstractMediaSource mediaSource) {

  }

  @Override
  public void onMediaUpdate(PlayerManager playerManager, AbstractMediaSource currentMediaSource) {

  }

  public class PlayerBinder extends Binder {
    public PlayerManager getPlayerManager() {
      return PlayerService.this.playerManager;
    }

    public AbstractMediaSource getCurrentMediaSource() {
      return playerManager.getCurrentMediaSource();
    }

    public void addListener(PlayerManagerListener listener) {
      playerManager.addListener(listener);
    }

    public void removeListener(PlayerManagerListener listener) {
      playerManager.removeListener(listener);
    }

    public boolean isPlaying() {
      return playerManager.isPlaying();
    }
  }

  public static boolean isRunning() {
    return running;
  }

  private BroadcastReceiver headsetDisconnected = new BroadcastReceiver() {
    private static final String TAG = "headsetDisconnected";
    private static final int UNPLUGGED = 0;
    @Override
    public void onReceive(Context context, Intent intent) {
      if (intent.getAction() == Intent.ACTION_HEADSET_PLUG) {
        int state = intent.getIntExtra("state", -1);
        if (state != -1) {
          Log.d(TAG, "Headset plug event. State is " + state);
          if (state == UNPLUGGED) {
            Log.d(TAG, "Headset was unplugged during playback.");
            playerManager.pause();
          }
        } else {
          Log.e(TAG, "Received invalid ACTION_HEADSET_PLUG intent");
        }
      }
    }
  };

  private BroadcastReceiver audioBecomingNoisy = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      Log.d(TAG, "Pausing playback because audio is becoming noisy");
      playerManager.pause();
    }
  };

}
