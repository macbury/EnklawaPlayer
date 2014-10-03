package macbury.enklawa.managers.player.sources;

import android.net.Uri;

/**
 * Created by macbury on 03.10.14.
 */
public class RadioMediaSource extends AbstractMediaSource {
  private int position;

  @Override
  public String getTitle() {
    return null;
  }

  @Override
  public String getSummary() {
    return null;
  }

  @Override
  public Uri getMediaUri() {
    return null;
  }

  @Override
  public Uri getPreviewArtUri() {
    return null;
  }

  @Override
  public boolean isLiveStream() {
    return true;
  }

  @Override
  public void onPlay() {

  }

  @Override
  public void onPause() {

  }

  @Override
  public void onFinishPlayback() {

  }

  @Override
  public int getPosition() {
    return position;
  }

  @Override
  public void setPosition(int duration) {
    this.position = duration;
  }

  @Override
  public int getDuration() {
    return position;
  }
}
