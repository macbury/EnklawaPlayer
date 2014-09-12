package macbury.enklawa.navigation_drawer.items;

import android.app.Fragment;
import android.content.Context;

import be.webelite.ion.Icon;
import macbury.enklawa.navigation_drawer.NavBaseItem;

/**
 * Created by macbury on 11.09.14.
 */
public abstract class NavItemFragment extends NavBaseItem {

  public NavItemFragment(Context context) {
    super(context);
  }

  public abstract String getTitle();
  public abstract Icon getIcon();
  public abstract Fragment getFragment();

  @Override
  public int getType() {
    return 1;
  }
}
