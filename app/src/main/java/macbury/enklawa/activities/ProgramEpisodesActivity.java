package macbury.enklawa.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.View;

import macbury.enklawa.fragments.SettingsFragment;
import macbury.enklawa.fragments.main.episodes.NewestEpisodesFragment;

/**
 * Created by macbury on 15.09.14.
 */
public class ProgramEpisodesActivity extends FragmentActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getFragmentManager().beginTransaction()
            .replace(android.R.id.content, new NewestEpisodesFragment())
            .commit();
  }
}
