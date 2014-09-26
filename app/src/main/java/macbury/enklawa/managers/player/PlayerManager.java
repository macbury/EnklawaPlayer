package macbury.enklawa.managers.player;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.PowerManager;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import macbury.enklawa.db.models.EnqueueEpisode;
import macbury.enklawa.extensions.SleepTimer;
import macbury.enklawa.managers.Enklawa;

/**
 * Created by macbury on 19.09.14.
 */
public class PlayerManager implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnBufferingUpdateListener, SleepTimer.SleepTimerListener, MediaPlayer.OnInfoListener {
  private static final String TAG = "PlayerManager";
  private final SleepTimer fakeLoop;
  private EnqueueEpisode currentEnqueuedEpisode;
  private final Context context;
  private final MediaPlayer player;
  private final ArrayList<EnqueueEpisode> queue;
  private Mode mode;

  public enum Mode {
    Initialized, Preparing, Playing, Finished, Paused
  }

  public PlayerManager(Context context) {
    this.context  = context;
    this.fakeLoop = new SleepTimer(1000, this);
    this.player   = new MediaPlayer();
    this.queue    = new ArrayList<EnqueueEpisode>();

    player.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK);
    player.setAudioStreamType(AudioManager.STREAM_MUSIC);
    player.setOnBufferingUpdateListener(this);
    player.setOnPreparedListener(this);
    player.setOnCompletionListener(this);
    player.setOnErrorListener(this);
    player.setOnInfoListener(this);
    fakeLoop.start();
  }

  public void add(EnqueueEpisode enqueueEpisode) {
    stop();
    if (queue.indexOf(enqueueEpisode) == -1) {
      queue.add(0, enqueueEpisode);
    }
    if (!isRunning()) {
      next();
    }
  }

  public void set(ArrayList<EnqueueEpisode> episodes) {
    for (EnqueueEpisode episode : episodes) {
      if (queue.indexOf(episode) == -1) {
        queue.add(episode);
      }
    }

    if (!isRunning()) {
      next();
    }
  }

  private void next() {
    mode = Mode.Preparing;

    if (queue.size() > 0) {
      currentEnqueuedEpisode = queue.remove(0);
      try {
        Uri uri = Enklawa.current().storage.getEpisodeUri(currentEnqueuedEpisode.episode);
        Log.i(TAG, "Episode " + currentEnqueuedEpisode.episode.name + " is " + uri.toString());
        player.setDataSource(context, uri);
        player.prepareAsync();
      } catch (IOException e) {
        e.printStackTrace();
        finishPlayback();
        next();
      }
    } else {
      Log.i(TAG, "Finished all enqueed episodes");
      // finished all files!
    }
  }

  public boolean isRunning() {
    return currentEnqueuedEpisode != null;
  }

  public void destroy() {
    player.stop();
    player.release();
    fakeLoop.kill();
  }

  public void play() {
    if (mode != Mode.Preparing) {
      Log.i(TAG, "PLay");
      mode = Mode.Playing;
      player.start();
    }
  }

  public void pause() {
    if (player.isPlaying()) {
      Log.i(TAG, "pause");
      mode = Mode.Paused;
      player.pause();
    }
  }

  public void stop() {
    if (player.isPlaying()) {
      Log.i(TAG, "stop");
      player.stop();
      finishPlayback();
    }
  }

  private void finishPlayback() {
    mode = Mode.Finished;
    player.reset();
    Enklawa.current().db.queue.destroy(currentEnqueuedEpisode);
    currentEnqueuedEpisode = null;
  }

  @Override
  public void onPrepared(MediaPlayer mp) {
    Log.i(TAG, "onPrepared");
    mode =  Mode.Initialized;
    play();
  }

  @Override
  public void onBufferingUpdate(MediaPlayer mp, int percent) {

  }

  @Override
  public void onCompletion(MediaPlayer mp) {
    finishPlayback();
    next();
  }

  @Override
  public boolean onError(MediaPlayer mp, int what, int extra) {
    next();
    return true;
  }

  @Override
  public void onSleepTimerTick(SleepTimer timer) {
    Log.i(TAG, "Tick....");
  }

  @Override
  public boolean onInfo(MediaPlayer mp, int what, int extra) {
    Log.i(TAG, "what info= "+what);
    return false;
  }
}
