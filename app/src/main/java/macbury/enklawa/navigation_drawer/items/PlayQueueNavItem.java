package macbury.enklawa.navigation_drawer.items;

import android.app.Fragment;
import android.content.Context;

import be.webelite.ion.Icon;
import macbury.enklawa.R;

/**
 * Created by macbury on 22.09.14.
 */
public class PlayQueueNavItem extends NavItemFragment {
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
  public Fragment getFragment() {
    return null;
  }
}
