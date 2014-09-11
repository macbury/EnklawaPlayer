package macbury.enklawa.activities.main;

import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

import macbury.enklawa.R;
import macbury.enklawa.activities.main.MainActivity;

/**
 * Created by macbury on 11.09.14.
 */
public class MainActivityActionBarToggle extends ActionBarDrawerToggle {
  private final MainActivity activty;

  public MainActivityActionBarToggle(MainActivity activity, DrawerLayout drawerLayout) {
    super(activity, drawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close);
    this.activty = activity;
  }

  @Override
  public void onDrawerClosed(View view) {
    super.onDrawerClosed(view);
    activty.drawerStateChange(false);
  }

  @Override
  /** Called when a drawer has settled in a completely open state. */
  public void onDrawerOpened(View drawerView) {
    super.onDrawerOpened(drawerView);
    activty.drawerStateChange(true);
  }
}
