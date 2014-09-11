package macbury.enklawa.activities;

import android.app.ActionBar;
import android.app.Activity;
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

import fr.castorflex.android.smoothprogressbar.SmoothProgressDrawable;
import macbury.enklawa.R;
import macbury.enklawa.managers.ApplicationManager;
import macbury.enklawa.views.CoolProgress;

public class MainActivity extends Activity {
  private CoolProgress syncProgressBar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
    setProgressBarIndeterminateVisibility(true);
    setContentView(R.layout.activity_main);

    ActionBar actionBar = getActionBar();
    actionBar.setDisplayShowHomeEnabled(true);
    actionBar.setDisplayUseLogoEnabled(true);
    actionBar.setDisplayShowTitleEnabled(false);
    actionBar.setLogo(R.drawable.ic_logo_wide);

    this.syncProgressBar = new CoolProgress(this);
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
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
      ApplicationManager.current().services.syncPodService();
    }
    return super.onOptionsItemSelected(item);
  }
}
