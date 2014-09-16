package macbury.enklawa.activities.main;

import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import macbury.enklawa.R;
import macbury.enklawa.db.models.Program;
import macbury.enklawa.fragments.main.episodes.ProgramEpisodesFragment;
import macbury.enklawa.navigation_drawer.NavAdapter;
import macbury.enklawa.navigation_drawer.NavDivider;
import macbury.enklawa.navigation_drawer.items.AllProgramsNavItem;
import macbury.enklawa.navigation_drawer.items.DownloadedEpisodesNavItem;
import macbury.enklawa.navigation_drawer.items.FavoriteProgramNavItem;
import macbury.enklawa.navigation_drawer.items.ForumNavItem;
import macbury.enklawa.navigation_drawer.items.NavItemFragment;
import macbury.enklawa.navigation_drawer.items.NewestEpisodesNavItem;

/**
 * Created by macbury on 12.09.14.
 */
public class NavigationController implements AdapterView.OnItemClickListener {
  private final MainActivity          activity;
  private final ListView              navDrawerListView;
  private final DrawerLayout          mDrawerLayout;
  private NavAdapter                  drawerNavAdapter;
  private NewestEpisodesNavItem       newestEpisodesItemNav;
  private AllProgramsNavItem          allProgamsItemNav;
  private DownloadedEpisodesNavItem   downloadedItemNav;
  private NavDivider                  favoriteDividerNav;
  private ForumNavItem                forumNavItem;
  private NavigationListener          listener;
  private ProgramEpisodesFragment     programEpisodesFragment;

  public NavigationController(MainActivity activity, ListView navDrawerListView, DrawerLayout mDrawerLayout, NavigationListener listener) {
    this.activity                 = activity;
    this.listener                 = listener;
    this.navDrawerListView        = navDrawerListView;
    this.mDrawerLayout            = mDrawerLayout;
    this.forumNavItem             = new ForumNavItem(activity);
    this.favoriteDividerNav       = new NavDivider(activity, activity.getString(R.string.nav_divider_favorite));
    this.downloadedItemNav        = new DownloadedEpisodesNavItem(activity);
    this.allProgamsItemNav        = new AllProgramsNavItem(activity);
    this.newestEpisodesItemNav    = new NewestEpisodesNavItem(activity);
    this.drawerNavAdapter         = new NavAdapter(activity);

    refresh();
    navDrawerListView.setAdapter(drawerNavAdapter);
    navDrawerListView.setOnItemClickListener(this);
    setSelected(0);
  }

  public void refresh() {
    drawerNavAdapter.clear();
    drawerNavAdapter.add(newestEpisodesItemNav);
    drawerNavAdapter.add(allProgamsItemNav);
    drawerNavAdapter.add(downloadedItemNav);
    drawerNavAdapter.add(forumNavItem);

    drawerNavAdapter.add(favoriteDividerNav);
    for(Program program : listener.getNavigationControllerFavoritedPrograms()) {
      drawerNavAdapter.add(new FavoriteProgramNavItem(activity, program));
    }

    drawerNavAdapter.notifyDataSetChanged();
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    if (drawerNavAdapter.isDivider(position)) {
      return;
    }
    mDrawerLayout.closeDrawers();
    setSelected(position);
  }

  public void setSelected(int position) {
    if (position >= drawerNavAdapter.getCount()) {
      position = 0;
    }

    if (drawerNavAdapter.isNavItemFragment(position)) {
      NavItemFragment itemFragment = (NavItemFragment)drawerNavAdapter.getItem(position);
      this.listener.onNavigationFragmentSelect(itemFragment.getFragment());
    } else if (drawerNavAdapter.isNavFavoriteProgramItem(position)) {
      FavoriteProgramNavItem itemFragment = (FavoriteProgramNavItem)drawerNavAdapter.getItem(position);
      if (programEpisodesFragment == null) {
        programEpisodesFragment = new ProgramEpisodesFragment();
      }
      programEpisodesFragment.setProgram(itemFragment.getProgram());
      this.listener.onNavigationFragmentSelect(programEpisodesFragment);
    }
    this.drawerNavAdapter.setSelected(position);
  }

  public void setListener(NavigationListener listener) {
    this.listener = listener;
  }

  public int getSelected() {
    return drawerNavAdapter.getSelected();
  }
}
