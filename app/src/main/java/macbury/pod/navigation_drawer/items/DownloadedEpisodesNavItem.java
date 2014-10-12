package macbury.pod.navigation_drawer.items;

import android.app.Fragment;
import android.content.Context;

import be.webelite.ion.Icon;
import macbury.pod.R;
import macbury.pod.fragments.main.episodes.DownloadedEpisodesFragment;
import macbury.pod.managers.App;

/**
 * Created by macbury on 11.09.14.
 */
public class DownloadedEpisodesNavItem extends NavItemFragment {
  private DownloadedEpisodesFragment fragment;

  public DownloadedEpisodesNavItem(Context context) {
    super(context);
  }

  @Override
  public String getTitle() {
    return context.getString(R.string.nav_downloaded_episodes);
  }

  @Override
  public Icon getIcon() {
    return Icon.ion_android_download;
  }

  @Override
  public Fragment getFragment() {
    if (fragment == null) {
      fragment = new DownloadedEpisodesFragment();
    }
    return fragment;
  }

  @Override
  public int getCount() {
    return App.current().db.episodeFiles.countDownloadedOrPendingOrFailed();
  }

  @Override
  public int getColorResource() {
    return context.getResources().getColor(R.color.downloaded_episodes);
  }
}
