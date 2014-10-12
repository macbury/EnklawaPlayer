package macbury.pod.fragments.player;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.michaelevans.colorart.library.ColorArt;
import org.michaelevans.colorart.library.FadingImageView;

import macbury.pod.R;
import macbury.pod.db.models.Episode;

public class PlayerArtworkAndInfoFragment extends Fragment {
  private static final String TAG = "PlayerArtworkAndInfoFragment";
  private Episode         episode;
  private FadingImageView previewImage;
  private TextView        titleLabel;
  private TextView        dateLabel;
  private TextView        descriptionLabel;

  public PlayerArtworkAndInfoFragment() {

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view         = inflater.inflate(R.layout.fragment_player_artwork_and_info, container, false);
    previewImage      = (FadingImageView) view.findViewById(R.id.episode_preview);
    titleLabel        = (TextView) view.findViewById(R.id.episode_title);
    dateLabel         = (TextView) view.findViewById(R.id.episode_date);
    descriptionLabel  = (TextView) view.findViewById(R.id.episode_description);
    return view;
  }

  @Override
  public void onResume() {
    super.onResume();
    Ion.with(this).load(episode.image).intoImageView(previewImage);
    Ion.with(this).load(episode.image).asBitmap().setCallback(new FutureCallback<Bitmap>() {
      @Override
      public void onCompleted(Exception e, Bitmap result) {
        adaptColors(result);
      }
    });
    titleLabel.setText(episode.name);
    descriptionLabel.setText(episode.description);
    dateLabel.setText(DateUtils.formatDateTime(getActivity(), episode.pubDate.getTime(), DateUtils.FORMAT_SHOW_DATE));
  }

  private void adaptColors(Bitmap result) {
    ColorArt colorArt = new ColorArt(result);

    int backgroundColor = colorArt.getBackgroundColor();
    float hsb[]         = new float[3];
    Color.RGBToHSV(Color.red(backgroundColor), Color.green(backgroundColor), Color.blue(backgroundColor), hsb);
    float brightness    = hsb[2];

    if (brightness < 0.5) {
      Log.i(TAG, "Background is black");
      getActivity().setTheme(R.style.PlayerActionBarTheme_Dark);
    } else {
      Log.i(TAG, "Background is light");
      getActivity().setTheme(R.style.PlayerActionBarTheme_Light);
    }

    getView().setBackgroundColor(colorArt.getBackgroundColor());
    previewImage.setBackgroundColor(colorArt.getBackgroundColor(), FadingImageView.FadeSide.BOTTOM);
    titleLabel.setTextColor(colorArt.getPrimaryColor());
    dateLabel.setTextColor(colorArt.getDetailColor());
    descriptionLabel.setTextColor(colorArt.getSecondaryColor());

    previewImage.refreshDrawableState();
  }

  public void setEpisode(Episode episode) {
    this.episode = episode;
  }

  public Episode getEpisode() {
    return episode;
  }
}
