package macbury.enklawa.managers.player.sources;

import android.net.Uri;

import macbury.enklawa.R;
import macbury.enklawa.managers.Enklawa;

/**
 * Created by macbury on 03.10.14.
 */
public class RadioMediaSource extends AbstractMediaSource {
  private int position;

  private String getString(int resid) {
    return Enklawa.current().getResources().getString(resid);
  }

  @Override
  public String getTitle() {
    return getString(R.string.radio_title);
  }

  @Override
  public String getSummary() {
    return getString(R.string.radio_summary);
  }

  @Override
  public Uri getMediaUri() {
    return Enklawa.current().settings.getRadioURI();
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

  @Override
  public boolean equals(Object o) {
    return RadioMediaSource.class.isInstance(o);
  }
}
