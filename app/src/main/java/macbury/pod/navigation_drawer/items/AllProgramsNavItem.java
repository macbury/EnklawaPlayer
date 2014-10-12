package macbury.pod.navigation_drawer.items;

import android.app.Fragment;
import android.content.Context;

import be.webelite.ion.Icon;
import macbury.pod.R;
import macbury.pod.fragments.main.AllProgramsFragment;

/**
 * Created by macbury on 11.09.14.
 */
public class AllProgramsNavItem extends NavItemFragment {
  private AllProgramsFragment fragment;

  public AllProgramsNavItem(Context context) {
    super(context);
  }

  @Override
  public int getColorResource() {
    return context.getResources().getColor(R.color.all_programs_color);
  }

  @Override
  public String getTitle() {
    return context.getString(R.string.nav_all_programs);
  }

  @Override
  public Icon getIcon() {
    return Icon.ion_headphone;
  }

  @Override
  public Fragment getFragment() {
    if (fragment == null) {
      fragment = new AllProgramsFragment();
    }
    return fragment;
  }
}
