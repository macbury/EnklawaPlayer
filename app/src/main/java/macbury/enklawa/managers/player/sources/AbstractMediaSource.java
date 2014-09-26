package macbury.enklawa.managers.player.sources;

import android.net.Uri;

/**
 * Created by macbury on 25.09.14.
 */
public abstract class AbstractMediaSource {

  private int bufferring;

  public abstract String getTitle();
  public abstract String getSummary();
  public abstract Uri getMediaUri();
  public abstract Uri getPreviewArtUri();
  public abstract boolean isLiveStream();

  public abstract void onStart();
  public abstract void onPause();
  public abstract void setDuration(int duration);
  public abstract void onFinishPlayback();


  public void setBufferring(int bufferring) {
    this.bufferring = bufferring;
  }

  public int getBufferring() {
    return bufferring;
  }
}
