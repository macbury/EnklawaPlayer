package macbury.enklawa.adapters.navigation;

import android.app.Fragment;
import android.content.Context;

import macbury.enklawa.R;

/**
 * Created by macbury on 11.09.14.
 */
public class DownloadedEpisodesNavItem extends NavItemFragment {

  public DownloadedEpisodesNavItem(Context context) {
    super(context);
  }

  @Override
  public String getTitle() {
    return context.getString(R.string.nav_downloaded_episodes);
  }

  @Override
  public int getIcon() {
    return 0;
  }

  @Override
  public Fragment getFragment() {
    return null;
  }
}
