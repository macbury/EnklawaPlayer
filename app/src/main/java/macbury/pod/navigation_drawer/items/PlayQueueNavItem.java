package macbury.pod.navigation_drawer.items;

import android.app.Fragment;
import android.content.Context;

import be.webelite.ion.Icon;
import macbury.pod.R;
import macbury.pod.fragments.main.episodes.PlaylistFragment;
import macbury.pod.managers.App;

/**
 * Created by macbury on 22.09.14.
 */
public class PlayQueueNavItem extends NavItemFragment {
  private PlaylistFragment fragment;

  public PlayQueueNavItem(Context context) {
    super(context);
  }

  @Override
  public String getTitle() {
    return context.getString(R.string.nav_play_queue);
  }

  @Override
  public Icon getIcon() {
    return Icon.ion_music_note;
  }

  @Override
  public int getCount() {
    return (int) App.current().db.queue.count();
  }

  @Override
  public int getColorResource() {
    return context.getResources().getColor(R.color.queue_episodes);
  }

  @Override
  public Fragment getFragment() {
    if (fragment == null) {
      fragment = new PlaylistFragment();
    }
    return fragment;
  }
}
