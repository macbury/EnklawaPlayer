package macbury.enklawa.activities.player;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.michaelevans.colorart.library.ColorArt;
import org.michaelevans.colorart.library.FadingImageView;

import macbury.enklawa.R;
import macbury.enklawa.db.models.Episode;
import macbury.enklawa.fragments.player.PlayerArtworkAndInfoFragment;
import macbury.enklawa.fragments.player.PlayerControllerFragment;
import macbury.enklawa.managers.Enklawa;
import macbury.enklawa.managers.player.PlayerManager;
import macbury.enklawa.managers.player.PlayerManagerListener;
import macbury.enklawa.managers.player.sources.AbstractMediaSource;
import macbury.enklawa.managers.player.sources.EpisodeMediaSource;
import macbury.enklawa.services.PlayerService;

public class PlayerActivity extends Activity implements PlayerManagerListener {
  private static final String TAG                 = "PlayerActivity";
  private static final String CURRENT_EPISODE_ID  = "CURRENT_EPISODE_ID";
  private Episode episode;
  private View mainView;
  private PlayerControllerFragment playerFragmentController;
  private SystemBarTintManager tintManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_player);

    mainView          = (View) findViewById(R.id.player_background_view);


    boolean restoringState = savedInstanceState != null;

    int newEpisodeId  = -1;
    if (restoringState) {
      newEpisodeId = savedInstanceState.getInt(CURRENT_EPISODE_ID);
    } else {
      newEpisodeId = Enklawa.current().intents.getEpisodeId(getIntent());
    }

    Episode episode   = Enklawa.current().db.episodes.find(newEpisodeId);

    playerFragmentController = new PlayerControllerFragment();
    FragmentManager fm       = getFragmentManager();
    fm.beginTransaction().replace(R.id.player_frame, playerFragmentController).commit();

    setEpisode(episode);
    if (restoringState) {
      Enklawa.current().services.player();
    } else {
      Enklawa.current().services.playEpisode(episode);
    }
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

  public void setEpisode(Episode newEpisode) {
    if (newEpisode == episode) {
      return;
    }
    this.episode = newEpisode;

    PlayerArtworkAndInfoFragment playerArtworkAndInfoFragment = new PlayerArtworkAndInfoFragment();
    playerArtworkAndInfoFragment.setEpisode(episode);
    FragmentManager fm       = getFragmentManager();
    fm.beginTransaction()
      .replace(R.id.artwork_frame, playerArtworkAndInfoFragment)
      .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
    .commit();


    getActionBar().setTitle("");
    invalidateOptionsMenu();
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
    Episode episode        = ems.getEpisode();
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

  @Override
  public void onMediaError(PlayerManager playerManager, int extra) {

  }
}
