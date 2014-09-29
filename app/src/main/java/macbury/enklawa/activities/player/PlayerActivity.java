package macbury.enklawa.activities.player;

import android.app.Activity;
import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.michaelevans.colorart.library.ColorArt;
import org.michaelevans.colorart.library.FadingImageView;

import macbury.enklawa.R;
import macbury.enklawa.db.models.Episode;
import macbury.enklawa.fragments.PlayerControllerFragment;
import macbury.enklawa.managers.Enklawa;

public class PlayerActivity extends Activity {

  private static final String TAG = "PlayerActivity";
  private FadingImageView previewImage;
  private Episode episode;
  private TextView titleLabel;
  private TextView descriptionLabel;
  private TextView dateLabel;
  private View mainView;
  private PlayerControllerFragment playerFragmentController;
  private SystemBarTintManager tintManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_player);

    mainView          = (View) findViewById(R.id.player_background_view);
    previewImage      = (FadingImageView) findViewById(R.id.episode_preview);
    titleLabel        = (TextView) findViewById(R.id.episode_title);
    dateLabel         = (TextView) findViewById(R.id.episode_date);
    descriptionLabel  = (TextView) findViewById(R.id.episode_description);
    int newEpisodeId  = Enklawa.current().intents.getEpisodeId(getIntent());
    Episode episode   = Enklawa.current().db.episodes.find(newEpisodeId);

    playerFragmentController = new PlayerControllerFragment();
    FragmentManager fm       = getFragmentManager();
    fm.beginTransaction().replace(R.id.player_frame, playerFragmentController).commit();

    setEpisode(episode);
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    playerFragmentController.stopPlayerIfPaused();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.player, menu);
    MenuItem item = menu.findItem(R.id.menu_item_share);

    // Fetch and store ShareActionProvider
    ShareActionProvider mShareActionProvider = (ShareActionProvider) item.getActionProvider();
    mShareActionProvider.setShareIntent(Enklawa.current().intents.shareUrl(episode.link));
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    return super.onOptionsItemSelected(item);
  }

  public void setEpisode(Episode episode) {
    this.episode = episode;
    Enklawa.current().db.queue.createFromEpisode(episode);
    Enklawa.current().services.playEpisodeStream(episode);
    updateUIForEpisode();

  }

  private void updateUIForEpisode() {
    Ion.with(this).load(episode.image).intoImageView(previewImage);
    Ion.with(this).load(episode.image).asBitmap().setCallback(new FutureCallback<Bitmap>() {
      @Override
      public void onCompleted(Exception e, Bitmap result) {
        adaptColors(result);
      }
    });
    titleLabel.setText(episode.name);
    descriptionLabel.setText(episode.description);
    dateLabel.setText(DateUtils.formatDateTime(this, episode.pubDate.getTime(), DateUtils.FORMAT_SHOW_DATE));
    getActionBar().setTitle("");
    invalidateOptionsMenu();
  }

  private void adaptColors(Bitmap result) {
    ColorArt colorArt = new ColorArt(result);

    int backgroundColor = colorArt.getBackgroundColor();
    float hsb[]         = new float[3];
    Color.RGBToHSV(Color.red(backgroundColor), Color.green(backgroundColor), Color.blue(backgroundColor), hsb);
    float brightness    = hsb[2];

    if (brightness < 0.5) {
      Log.i(TAG, "Background is black");
      setTheme(R.style.PlayerActionBarTheme_Dark);
    } else {
      Log.i(TAG, "Background is light");
      setTheme(R.style.PlayerActionBarTheme_Light);
    }

   // tintManager.setStatusBarTintColor(colorArt.getBackgroundColor());
    mainView.setBackgroundColor(colorArt.getBackgroundColor());
    previewImage.setBackgroundColor(colorArt.getBackgroundColor(), FadingImageView.FadeSide.BOTTOM );
    titleLabel.setTextColor(colorArt.getPrimaryColor());
    dateLabel.setTextColor(colorArt.getDetailColor());
    descriptionLabel.setTextColor(colorArt.getSecondaryColor());
  }

  public Episode getEpisode() {
    return episode;
  }
}
