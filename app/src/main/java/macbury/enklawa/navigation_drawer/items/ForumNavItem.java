package macbury.enklawa.navigation_drawer.items;

import android.app.Fragment;
import android.content.Context;

import be.webelite.ion.Icon;
import macbury.enklawa.R;

/**
 * Created by macbury on 12.09.14.
 */
public class ForumNavItem extends NavItemFragment {
  public ForumNavItem(Context context) {
    super(context);
  }

  @Override
  public int getColorResource() {
    return 0;
  }

  @Override
  public String getTitle() {
    return context.getString(R.string.nav_forum);
  }

  @Override
  public Icon getIcon() {
    return Icon.ion_chatbubble;
  }

  @Override
  public Fragment getFragment() {
    return null;
  }
}
