package macbury.enklawa.activities.player;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
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
import macbury.enklawa.managers.player.PlayerManager;
import macbury.enklawa.managers.player.PlayerManagerListener;
import macbury.enklawa.managers.player.sources.AbstractMediaSource;
import macbury.enklawa.managers.player.sources.EpisodeMediaSource;
import macbury.enklawa.services.PlayerService;

public class PlayerActivity extends Activity implements PlayerManagerListener {
  private static final String TAG                 = "PlayerActivity";
  private static final String CURRENT_EPISODE_ID  = "CURRENT_EPISODE_ID";
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
    int newEpisodeId  = -1;
    if (savedInstanceState != null && savedInstanceState.getInt(CURRENT_EPISODE_ID) != 0) {
      newEpisodeId = savedInstanceState.getInt(CURRENT_EPISODE_ID);
    } else {
      newEpisodeId = Enklawa.current().intents.getEpisodeId(getIntent());
    }

    Episode episode   = Enklawa.current().db.episodes.find(newEpisodeId);

    playerFragmentController = new PlayerControllerFragment();
    FragmentManager fm       = getFragmentManager();
    fm.beginTransaction().replace(R.id.player_frame, playerFragmentController).commit();

    setEpisode(episode);

    Enklawa.current().services.playEpisodeStream(episode);
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);

    outState.putInt(CURRENT_EPISODE_ID, episode.id);
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    playerFragmentController.stopPlayerIfPaused();
  }

  @Override
  protected void onResume() {
    super.onResume();
    bindService(Enklawa.current().intents.player(), playerManagerServiceConnection, Context.BIND_AUTO_CREATE);
  }

  @Override
  protected void onPause() {
    super.onPause();
    unbindService(playerManagerServiceConnection);
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

    mainView.setBackgroundColor(colorArt.getBackgroundColor());
    previewImage.setBackgroundColor(colorArt.getBackgroundColor(), FadingImageView.FadeSide.BOTTOM);
    titleLabel.setTextColor(colorArt.getPrimaryColor());
    dateLabel.setTextColor(colorArt.getDetailColor());
    descriptionLabel.setTextColor(colorArt.getSecondaryColor());

    previewImage.refreshDrawableState();
  }

  public Episode getEpisode() {
    return episode;
  }

  private PlayerService.PlayerBinder playerBinder;
  private ServiceConnection playerManagerServiceConnection = new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
      PlayerActivity.this.playerBinder = (PlayerService.PlayerBinder)service;
      playerBinder.addListener(PlayerActivity.this);
      updateUIForEpisode();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
      Log.i(TAG, "Player service disconnected!");
      playerBinder.removeListener(PlayerActivity.this);
      playerBinder = null;
    }
  };

  @Override
  public void onInitialize(PlayerManager manager, AbstractMediaSource mediaSource) {
    EpisodeMediaSource ems = (EpisodeMediaSource)mediaSource;
    Episode episode        = ems.getEpisode().episode;
    setEpisode(episode);
    NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    mNotificationManager.cancel(episode.id);
  }

  @Override
  public void onFinishAll(PlayerManager manager) {
    finish();
  }

  @Override
  public void onPlay(PlayerManager manager, AbstractMediaSource mediaSource) {

  }

  @Override
  public void onPause(PlayerManager manager, AbstractMediaSource mediaSource) {

  }

  @Override
  public void onFinish(PlayerManager manager, AbstractMediaSource mediaSource) {

  }

  @Override
  public void onBufferMedia(PlayerManager manager, AbstractMediaSource mediaSource) {

  }

  @Override
  public void onMediaUpdate(PlayerManager playerManager, AbstractMediaSource currentMediaSource) {

  }
}
