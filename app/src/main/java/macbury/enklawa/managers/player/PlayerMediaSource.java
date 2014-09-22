package macbury.enklawa.managers.player;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.PowerManager;

import java.io.File;
import java.net.URI;

/**
 * Created by macbury on 19.09.14.
 */
public abstract class PlayerMediaSource implements MediaPlayer.OnPreparedListener, MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnErrorListener {
  public enum PlayerStatus {
    Pending, Buffering, Playing, Pause
  }
  private MediaPlayer mediaPlayer;
  private PlayerStatus status = PlayerStatus.Pending;

  public PlayerMediaSource(Context context) {
    mediaPlayer = new MediaPlayer();
    mediaPlayer.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK);
    mediaPlayer.setOnPreparedListener(this);
    mediaPlayer.setOnCompletionListener(this);
    mediaPlayer.setOnBufferingUpdateListener(this);
    mediaPlayer.setOnSeekCompleteListener(this);
    mediaPlayer.setOnErrorListener(this);
  }

  public void play() {
    mediaPlayer.prepareAsync();
    mediaPlayer.start();
  }

  @Override
  public void onPrepared(MediaPlayer mp) {

  }

  @Override
  public void onSeekComplete(MediaPlayer mp) {

  }

  @Override
  public void onCompletion(MediaPlayer mp) {

  }

  @Override
  public void onBufferingUpdate(MediaPlayer mp, int percent) {

  }

  @Override
  public boolean onError(MediaPlayer mp, int what, int extra) {
    return false;
  }

  public void release() {
    mediaPlayer.release();
  }

  public abstract boolean isRemote();
  public abstract URI     getURL();
  public abstract File    getFile();
  public abstract URI     getArtWork();
  public abstract String  getTitle();
  public abstract String  getAuthor();
  public abstract String  getDetails();
}
