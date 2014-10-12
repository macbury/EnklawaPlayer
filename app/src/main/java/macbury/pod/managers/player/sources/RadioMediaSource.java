package macbury.pod.managers.player.sources;

import android.net.Uri;

import macbury.pod.R;
import macbury.pod.managers.App;

/**
 * Created by macbury on 03.10.14.
 */
public class RadioMediaSource extends AbstractMediaSource {
  private int position;

  private String getString(int resid) {
    return App.current().getResources().getString(resid);
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
    return App.current().settings.getRadioURI();
  }

  @Override
  public Uri getPreviewArtUri() {
    return Uri.parse("android.resource://macbury.enklawa/drawable/radio");
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

  public String getChatURL() {
    return "http://www7.cbox.ws/box/?boxid=470567&boxtag=4tj2gk&sec=main";
  }

  @Override
  public boolean equals(Object o) {
    return RadioMediaSource.class.isInstance(o);
  }
}
