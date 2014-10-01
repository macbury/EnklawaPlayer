package macbury.enklawa.managers.player;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.PowerManager;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import macbury.enklawa.db.models.EnqueueEpisode;
import macbury.enklawa.extensions.SleepTimer;
import macbury.enklawa.managers.Enklawa;
import macbury.enklawa.managers.player.sources.AbstractMediaSource;
import macbury.enklawa.managers.player.sources.EpisodeMediaSource;

/**
 * Created by macbury on 19.09.14.
 */
public class PlayerManager implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnBufferingUpdateListener, SleepTimer.SleepTimerListener, MediaPlayer.OnInfoListener {
  private static final String TAG = "PlayerManager";
  private final SleepTimer fakeLoop;
  private AbstractMediaSource currentMediaSource;
  private final Context context;
  private final MediaPlayer player;
  private boolean preparing;
  private ArrayList<PlayerManagerListener> listeners;

  public PlayerManager(Context context) {
    this.context   = context;
    this.fakeLoop  = new SleepTimer(1000, this);
    this.player    = new MediaPlayer();
    this.listeners = new ArrayList<PlayerManagerListener>();

    player.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK);
    player.setAudioStreamType(AudioManager.STREAM_MUSIC);
    player.setOnBufferingUpdateListener(this);
    player.setOnPreparedListener(this);
    player.setOnCompletionListener(this);
    player.setOnErrorListener(this);
    player.setOnInfoListener(this);
    fakeLoop.start();
  }

  public void start() {
    if (isRunning()) {
      play();
    } else {
      next();
    }
  }

  public PlaybackStatus getState() {
    if (isRunning()) {
      return currentMediaSource.getStatus();
    } else {
      return PlaybackStatus.Pending;
    }
  }

  public void addListener(PlayerManagerListener listener) {
    if (listeners.indexOf(listener) == -1) {
      listeners.add(listener);
    }
  }

  public void removeListener(PlayerManagerListener listener) {
    if (listeners.indexOf(listener) != -1) {
      listeners.remove(listener);
    }
  }

  private void next() {
    EnqueueEpisode nextToPlay = Enklawa.current().db.queue.nextToPlay();
    if (nextToPlay == null) {
      cancel();
    } else {
      currentMediaSource = new EpisodeMediaSource(nextToPlay);
      preparing          = true;
      try {
        Log.i(TAG, "Episode " + currentMediaSource.getTitle() + " is " + currentMediaSource.getMediaUri().toString());
        for (PlayerManagerListener listener : listeners) {
          listener.onInitialize(this, currentMediaSource);
        }
        player.reset();
        player.setDataSource(context, currentMediaSource.getMediaUri());
        player.prepareAsync();
      } catch (IOException e) {
        e.printStackTrace();
        finishPlayback();
        next();
      }
    }
  }

  public boolean isRunning() {
    return currentMediaSource != null;
  }

  public void destroy() {
    fakeLoop.kill();
    player.stop();
    player.release();
  }

  public void play() {
    if (!preparing || !player.isPlaying()) {
      Log.i(TAG, "Play");
      currentMediaSource.onPlay();
      player.start();

      for (PlayerManagerListener listener : listeners) {
        listener.onPlay(this, currentMediaSource);
      }
    }
  }

  public void pause() {
    if (isPlaying()) {
      Log.i(TAG, "pause");
      currentMediaSource.onPause();
      player.pause();

      for (PlayerManagerListener listener : listeners) {
        listener.onPause(this, currentMediaSource);
      }
    }
  }

  public void stop() {
    if (player.isPlaying()) {
      Log.i(TAG, "stop");
      player.stop();
      for (PlayerManagerListener listener : listeners) {
        listener.onFinish(this, currentMediaSource);
      }
      finishPlayback();
    }
  }

  private void finishPlayback() {
    player.reset();
    currentMediaSource.onFinishPlayback();
    currentMediaSource = null;
  }

  @Override
  public void onPrepared(MediaPlayer mp) {
    Log.i(TAG, "onPrepared seeking to: " + currentMediaSource.getPosition());
    preparing = false;
    seekTo(currentMediaSource.getPosition());
    play();
  }

  @Override
  public void onBufferingUpdate(MediaPlayer mp, int percent) {
    for (PlayerManagerListener listener : listeners) {
      listener.onBufferMedia(this, currentMediaSource);
    }
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
    if (currentMediaSource != null) {
      currentMediaSource.setPosition(player.getCurrentPosition());
    }
    for (PlayerManagerListener listener : listeners) {
      if (player.isPlaying()) {
        listener.onMediaUpdate(this, currentMediaSource);
      }
    }
  }

  @Override
  public boolean onInfo(MediaPlayer mp, int what, int extra) {
    Log.i(TAG, "what info= "+what);
    return false;
  }

  public AbstractMediaSource getCurrentMediaSource() {
    return currentMediaSource;
  }

  public boolean isPlaying() {
    return player.isPlaying();
  }

  public boolean isPreparing() {
    return preparing;
  }

  public void restart() {
    if (isRunning()) {
      pause();
    }
    next();
  }

  public void cancel() {
    stop();
    Log.i(TAG, "Finished all enqueed episodes");
    for (PlayerManagerListener listener : listeners) {
      listener.onFinishAll(this);
    }
  }

  public boolean is(EnqueueEpisode enqueueEpisode) {
    return isRunning() && currentMediaSource.equals(enqueueEpisode);
  }

  public void seekTo(int duration) {
    if (isRunning()) {
      player.seekTo(duration);
      currentMediaSource.setPosition(duration);
    }
  }

  public boolean isPaused() {
    return !isPlaying();
  }
}
