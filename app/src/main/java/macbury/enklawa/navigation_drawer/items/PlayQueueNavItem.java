package macbury.enklawa.navigation_drawer.items;

import android.app.Fragment;
import android.content.Context;

import be.webelite.ion.Icon;
import macbury.enklawa.R;
import macbury.enklawa.fragments.main.episodes.PlaylistFragment;
import macbury.enklawa.managers.Enklawa;

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
    return (int)Enklawa.current().db.queue.count();
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
