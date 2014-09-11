package macbury.enklawa.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.negusoft.holoaccent.activity.AccentActivity;

import fr.castorflex.android.smoothprogressbar.SmoothProgressDrawable;
import macbury.enklawa.R;
import macbury.enklawa.managers.ApplicationManager;
import macbury.enklawa.services.SyncPodService;
import macbury.enklawa.views.CoolProgress;

public class MainActivity extends AccentActivity {
  private CoolProgress syncProgressBar;

  BroadcastReceiver syncReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      if (SyncPodService.isRunning()) {
        syncProgressBar.setIndeterminate(true);
        syncProgressBar.progressiveStart();
      } else {
        syncProgressBar.setIndeterminate(false);
        syncProgressBar.progressiveStop();
      }
      MainActivity.this.invalidateOptionsMenu();
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
    setProgressBarIndeterminateVisibility(true);
    setContentView(R.layout.activity_main);

    // TODO move to style
    ActionBar actionBar = getActionBar();
    actionBar.setDisplayShowHomeEnabled(true);
    actionBar.setDisplayUseLogoEnabled(true);
    actionBar.setDisplayShowTitleEnabled(false);
    actionBar.setLogo(R.drawable.ic_logo_wide);

    this.syncProgressBar = new CoolProgress(this);

  }

  @Override
  protected void onResume() {
    super.onResume();
    ApplicationManager.current().broadcasts.podSyncReceiver(this, syncReceiver);
  }

  @Override
  protected void onPause() {
    unregisterReceiver(syncReceiver);
    super.onPause();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);

    MenuItem syncMenuItem = menu.findItem(R.id.action_refresh);
    syncMenuItem.setVisible(!SyncPodService.isRunning());

    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
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
}
