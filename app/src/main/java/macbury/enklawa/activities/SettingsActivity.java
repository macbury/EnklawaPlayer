package macbury.enklawa.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import macbury.enklawa.fragments.SettingsFragment;

/**
 * Created by macbury on 09.09.14.
 */
public class SettingsActivity extends FragmentActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getFragmentManager().beginTransaction()
            .replace(android.R.id.content, new SettingsFragment())
            .commit();
  }
}
