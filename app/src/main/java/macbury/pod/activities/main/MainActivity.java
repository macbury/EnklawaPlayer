package macbury.pod.activities.main;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.negusoft.holoaccent.activity.AccentActivity;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.List;

import macbury.pod.R;
import macbury.pod.activities.DonateActivity;
import macbury.pod.db.models.Program;
import macbury.pod.fragments.player.ExternalPlayerFragment;
import macbury.pod.managers.App;
import macbury.pod.services.SyncPodService;

public class MainActivity extends AccentActivity implements NavigationListener {
  private static final String SAVE_STATE_SELECTED_NAVBAR_ITEM = "SAVE_STATE_SELECTED_NAVBAR_ITEM";

  private DrawerLayout                  mDrawerLayout;
  private ActionBarDrawerToggle         mDrawerToggle;
  private boolean                       drawerOpened;
  private ListView                      navDrawerListView;
  private NavigationController          navigationController;
  private View                          playerFrameView;
  private ExternalPlayerFragment playerControllerFragment;
  private View                          mainContainer;

  private void updateUI() {
    if (SyncPodService.isRunning()) {
    } else {
    }
    this.invalidateOptionsMenu();

    navigationController.refresh();
  }

  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    navigationController.setSelected(savedInstanceState.getInt(SAVE_STATE_SELECTED_NAVBAR_ITEM));
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putInt(SAVE_STATE_SELECTED_NAVBAR_ITEM, navigationController.getSelected());
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    SystemBarTintManager tintManager = new SystemBarTintManager(this);
    tintManager.setStatusBarTintEnabled(true);
    tintManager.setStatusBarTintResource(R.color.statusbar_color);
    playerFrameView               = (View)findViewById(R.id.player_frame);
    navDrawerListView             = (ListView) findViewById(R.id.left_drawer);
    mDrawerLayout                 = (DrawerLayout) findViewById(R.id.drawer_layout);
    mDrawerToggle                 = new MainActivityActionBarToggle(this, mDrawerLayout);
    mainContainer                 = findViewById(R.id.main_container);
    this.navigationController     = new NavigationController(this, navDrawerListView, mDrawerLayout, this);

    mDrawerLayout.setDrawerListener(mDrawerToggle);
    navigationController.setListener(this);

    ActionBar actionBar = getActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setHomeButtonEnabled(true);
    actionBar.setDisplayShowHomeEnabled(true);

    SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      mainContainer.setPadding(0, config.getPixelInsetTop(true), config.getPixelInsetRight(), config.getPixelInsetBottom());
      navDrawerListView.setPadding((int) getResources().getDimension(R.dimen.nav_bar_padding), config.getPixelInsetTop(true), (int) getResources().getDimension(R.dimen.nav_bar_padding), config.getPixelInsetBottom());
    }
    showPlayer();

    //MainActivityTutorial tutorial = new MainActivityTutorial(this);
  }

  private void showPlayer() {
    if (playerControllerFragment == null){
      playerControllerFragment = new ExternalPlayerFragment();
    }
    FragmentManager fragmentManager = getFragmentManager();
    fragmentManager.beginTransaction()
            .replace(R.id.player_frame, playerControllerFragment)
            .commit();
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    mDrawerToggle.onConfigurationChanged(newConfig);
  }

  @Override
  protected void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    mDrawerToggle.syncState();
  }

  @Override
  protected void onResume() {
    super.onResume();
    App app = App.current();
    app.broadcasts.podSyncReceiver(this, syncReceiver);
    app.broadcasts.favoriteProgramChangeReceiver(this, updateUIReceiver);
    updateUI();
  }

  @Override
  protected void onPause() {
    unregisterReceiver(syncReceiver);
    unregisterReceiver(updateUIReceiver);
    super.onPause();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    if (!drawerOpened) {
      getMenuInflater().inflate(R.menu.main, menu);

      MenuItem syncMenuItem = menu.findItem(R.id.action_refresh);
      syncMenuItem.setVisible(!SyncPodService.isRunning());

      MenuItem streamMenuItem = menu.findItem(R.id.action_stream);
      streamMenuItem.setVisible(true);
    }
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (mDrawerToggle.onOptionsItemSelected(item)) {
      return true;
    }

    int id = item.getItemId();
    if (id == R.id.action_stream) {
      startActivity(App.current().intents.playRadioActivity());
      return true;
    } else if (id == R.id.action_settings) {
      startActivity(App.current().intents.showSettingsActivity());
      return true;
    } else if (id == R.id.action_refresh) {
      startManualSync();
    } else if (id == R.id.action_donate) {
      donate();
    }
    return super.onOptionsItemSelected(item);
  }

  private void donate() {
    Intent donateIntent = new Intent(this, DonateActivity.class);
    startActivity(donateIntent);
  }

  @Override
  public void onBackPressed() {
    if (drawerOpened) {
      mDrawerLayout.closeDrawers();
    } else {
      super.onBackPressed();
    }
  }

  public void startManualSync() {
    App.current().services.syncPodService();
  }

  public void drawerStateChange(boolean opened) {
    drawerOpened = opened;
    this.invalidateOptionsMenu();
  }

  BroadcastReceiver syncReceiver     = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      MainActivity.this.updateUI();
    }
  };

  BroadcastReceiver updateUIReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) { MainActivity.this.updateUI(); }
  };

  @Override
  public void onNavigationFragmentSelect(Fragment fragment) {
    FragmentManager fragmentManager = getFragmentManager();
    fragmentManager.beginTransaction()
            .replace(R.id.content_frame, fragment)
            .commit();

  }

  @Override
  public List<Program> getNavigationControllerFavoritedPrograms() {
    return App.current().db.programs.allFavorited();
  }
}
