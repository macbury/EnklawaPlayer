package macbury.enklawa.activities.main;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ListView;

import com.negusoft.holoaccent.activity.AccentActivity;

import macbury.enklawa.R;
import macbury.enklawa.adapters.NavAdapter;
import macbury.enklawa.adapters.navigation.AllProgramsNavItem;
import macbury.enklawa.adapters.navigation.DownloadedEpisodesNavItem;
import macbury.enklawa.adapters.navigation.NewestEpisodesNavItem;
import macbury.enklawa.managers.ApplicationManager;
import macbury.enklawa.services.SyncPodService;
import macbury.enklawa.views.CoolProgress;

public class MainActivity extends AccentActivity {
  private CoolProgress                  syncProgressBar;
  private DrawerLayout                  mDrawerLayout;
  private ActionBarDrawerToggle         mDrawerToggle;
  private boolean                       drawerOpened;
  private ListView                      navDrawerListView;
  private NavAdapter                    drawerNavAdapter;
  private NewestEpisodesNavItem         newestEpisodesItemNav;
  private AllProgramsNavItem            allProgamsItemNav;
  private DownloadedEpisodesNavItem     downloadedItemNav;


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

    drawerNavAdapter.clear();
    drawerNavAdapter.add(newestEpisodesItemNav);
    drawerNavAdapter.add(allProgamsItemNav);
    drawerNavAdapter.add(downloadedItemNav);
    navDrawerListView.setAdapter(drawerNavAdapter);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
    setProgressBarIndeterminateVisibility(true);
    setContentView(R.layout.activity_main);

    this.downloadedItemNav        = new DownloadedEpisodesNavItem(this);
    this.allProgamsItemNav        = new AllProgramsNavItem(this);
    this.newestEpisodesItemNav    = new NewestEpisodesNavItem(this);
    this.drawerNavAdapter         = new NavAdapter(this);
    this.syncProgressBar          = new CoolProgress(this);
    navDrawerListView             = (ListView) findViewById(R.id.left_drawer);
    mDrawerLayout                 = (DrawerLayout) findViewById(R.id.drawer_layout);
    mDrawerToggle                 = new MainActivityActionBarToggle(this, mDrawerLayout);

    mDrawerLayout.setDrawerListener(mDrawerToggle);

    // TODO move to style
    ActionBar actionBar = getActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setHomeButtonEnabled(true);
    actionBar.setDisplayShowHomeEnabled(true);
    actionBar.setDisplayUseLogoEnabled(true);
    actionBar.setDisplayShowTitleEnabled(false);
    actionBar.setLogo(R.drawable.ic_logo_wide);
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
    ApplicationManager.current().broadcasts.podSyncReceiver(this, syncReceiver);
    updateUI();
  }

  @Override
  protected void onPause() {
    unregisterReceiver(syncReceiver);
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
      startActivity(ApplicationManager.current().intents.showSettingsActivity());
      return true;
    } else if (id == R.id.action_refresh) {
      syncProgressBar.setIndeterminate(true);
      startManualSync();
    }
    return super.onOptionsItemSelected(item);
  }

  public void startManualSync() {
    ApplicationManager.current().services.syncPodService();
  }

  public void drawerStateChange(boolean opened) {
    drawerOpened = opened;
    this.invalidateOptionsMenu();
  }

  BroadcastReceiver syncReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      MainActivity.this.updateUI();
    }
  };
}
