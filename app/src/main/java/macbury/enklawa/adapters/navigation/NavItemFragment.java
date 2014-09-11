package macbury.enklawa.adapters.navigation;

import android.app.Fragment;
import android.content.Context;

/**
 * Created by macbury on 11.09.14.
 */
public abstract class NavItemFragment extends NavBaseItem {

  public NavItemFragment(Context context) {
    super(context);
  }

  public abstract String getTitle();
  public abstract int getIcon();
  public abstract Fragment getFragment();

  @Override
  public int getType() {
    return 1;
  }
}
