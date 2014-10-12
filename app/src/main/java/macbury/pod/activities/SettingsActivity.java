package macbury.pod.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import macbury.pod.R;
import macbury.pod.activities.ext.AccentFragmentActivity;
import macbury.pod.fragments.SettingsFragment;

/**
 * Created by macbury on 09.09.14.
 */
public class SettingsActivity extends AccentFragmentActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getFragmentManager().beginTransaction()
            .replace(android.R.id.content, new SettingsFragment())
            .commit();
    getActionBar().setDisplayHomeAsUpEnabled(true);

    SystemBarTintManager tintManager = new SystemBarTintManager(this);
    tintManager.setStatusBarTintEnabled(true);
    tintManager.setStatusBarTintResource(R.color.statusbar_color);

    View mainContentView = findViewById(android.R.id.content);
    SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      mainContentView.setPadding(0, config.getPixelInsetTop(true), config.getPixelInsetRight(), config.getPixelInsetBottom());
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == android.R.id.home) {
      NavUtils.navigateUpFromSameTask(this);
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
