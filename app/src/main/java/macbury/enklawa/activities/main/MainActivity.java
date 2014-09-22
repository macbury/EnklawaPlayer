package macbury.enklawa.activities.main;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ListView;

import com.negusoft.holoaccent.activity.AccentActivity;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.List;

import macbury.enklawa.R;
import macbury.enklawa.db.models.Program;
import macbury.enklawa.fragments.PlayerControllerFragment;
import macbury.enklawa.managers.Enklawa;
import macbury.enklawa.services.SyncPodService;
import macbury.enklawa.views.CoolProgress;

public class MainActivity extends AccentActivity implements NavigationListener {
  private static final String SAVE_STATE_SELECTED_NAVBAR_ITEM = "SAVE_STATE_SELECTED_NAVBAR_ITEM";

  private CoolProgress                  syncProgressBar;
  private DrawerLayout                  mDrawerLayout;
  private ActionBarDrawerToggle         mDrawerToggle;
  private boolean                       drawerOpened;
  private ListView                      navDrawerListView;
  private NavigationController          navigationController;
  private View                          playerFrameView;
  private PlayerControllerFragment      playerControllerFragment;
  private View                          mainContainer;

  private void updateUI() {
    if (SyncPodService.isRunning()) {
      syncProgressBar.setIndeterminate(true);
      syncProgressBar.progressiveStart();
      syncProgressBar.setAlpha(1);
    } else {
      syncProgressBar.setIndeterminate(false);
      syncProgressBar.progressiveStop();
      syncProgressBar.setAlpha(0);
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
    this.syncProgressBar          = new CoolProgress(this);

    mDrawerLayout.setDrawerListener(mDrawerToggle);
    navigationController.setListener(this);

    ActionBar actionBar = getActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setHomeButtonEnabled(true);
    actionBar.setDisplayShowHomeEnabled(true);

    SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
    mainContainer.setPadding(0, config.getPixelInsetTop(true), config.getPixelInsetRight(), config.getPixelInsetBottom());
    navDrawerListView.setPadding((int)getResources().getDimension(R.dimen.nav_bar_padding), config.getPixelInsetTop(true), (int)getResources().getDimension(R.dimen.nav_bar_padding), config.getPixelInsetBottom());
  }

  private void showPlayer() {
    if (playerControllerFragment == null){
      playerControllerFragment = new PlayerControllerFragment();
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
    Enklawa app = Enklawa.current();
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
    }
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (mDrawerToggle.onOptionsItemSelected(item)) {
      return true;
    }

    int id = item.getItemId();
    if (id == R.id.action_settings) {
      startActivity(Enklawa.current().intents.showSettingsActivity());
      return true;
    } else if (id == R.id.action_refresh) {
      syncProgressBar.setIndeterminate(true);
      startManualSync();
    }
    return super.onOptionsItemSelected(item);
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
    Enklawa.current().services.syncPodService();
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
    return Enklawa.current().db.programs.allFavorited();
  }
}
