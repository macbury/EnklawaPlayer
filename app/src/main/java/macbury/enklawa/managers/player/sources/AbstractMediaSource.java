package macbury.enklawa.managers.player.sources;

import android.net.Uri;

import macbury.enklawa.managers.player.PlaybackStatus;

/**
 * Created by macbury on 25.09.14.
 */
public abstract class AbstractMediaSource {
  protected PlaybackStatus status = PlaybackStatus.Pending;

  public abstract String getTitle();
  public abstract String getSummary();
  public abstract Uri getMediaUri();
  public abstract Uri getPreviewArtUri();
  public abstract boolean isLiveStream();

  public abstract void onPlay();
  public abstract void onPause();
  public abstract void onFinishPlayback();

  public abstract int getPosition();
  public abstract void setPosition(int duration);
  public abstract int getDuration();

  public PlaybackStatus getStatus() {
    return status;
  }

  public void setStatus(PlaybackStatus status) {
    this.status = status;
  }
}
