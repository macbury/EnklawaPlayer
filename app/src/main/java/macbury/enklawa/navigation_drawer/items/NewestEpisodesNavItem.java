package macbury.enklawa.navigation_drawer.items;

import android.app.Fragment;
import android.content.Context;

import be.webelite.ion.Icon;
import macbury.enklawa.R;
import macbury.enklawa.fragments.main.episodes.NewestEpisodesFragment;

/**
 * Created by macbury on 11.09.14.
 */
public class NewestEpisodesNavItem extends NavItemFragment {
  private NewestEpisodesFragment fragment;

  public NewestEpisodesNavItem(Context context) {
    super(context);
  }

  @Override
  public String getTitle() {
    return context.getString(R.string.nav_newest_episodes);
  }

  @Override
  public Icon getIcon() {
    return Icon.ion_beer;
  }

  @Override
  public Fragment getFragment() {
    if (fragment == null) {
      fragment = new NewestEpisodesFragment();
    }
    return fragment;
  }
}
