package macbury.enklawa.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import macbury.enklawa.R;
import macbury.enklawa.managers.ApplicationManager;


public class MainActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ActionBar actionBar = getActionBar();
    actionBar.setDisplayShowHomeEnabled(true);
    actionBar.setDisplayUseLogoEnabled(true);
    actionBar.setDisplayShowTitleEnabled(false);
    actionBar.setLogo(R.drawable.ic_logo_wide);
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
      ApplicationManager.current().services.syncPodService();
    }
    return super.onOptionsItemSelected(item);
  }
}
