package macbury.pod.activities.main;

import android.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import macbury.pod.R;
import macbury.pod.db.models.Program;
import macbury.pod.fragments.main.episodes.ProgramEpisodesFragment;
import macbury.pod.navigation_drawer.NavAdapter;
import macbury.pod.navigation_drawer.NavDivider;
import macbury.pod.navigation_drawer.items.AllProgramsNavItem;
import macbury.pod.navigation_drawer.items.DownloadedEpisodesNavItem;
import macbury.pod.navigation_drawer.items.FavoriteProgramNavItem;
import macbury.pod.navigation_drawer.items.ForumNavItem;
import macbury.pod.navigation_drawer.items.NavItemFragment;
import macbury.pod.navigation_drawer.items.NewestEpisodesNavItem;
import macbury.pod.navigation_drawer.items.PlayQueueNavItem;

/**
 * Created by macbury on 12.09.14.
 */
public class NavigationController implements AdapterView.OnItemClickListener {
  private final MainActivity          activity;
  private final ListView              navDrawerListView;
  private final DrawerLayout          mDrawerLayout;
  private final PlayQueueNavItem      playQueueItemNav;
  private NavAdapter                  drawerNavAdapter;
  private NewestEpisodesNavItem       newestEpisodesItemNav;
  private AllProgramsNavItem          allProgamsItemNav;
  private DownloadedEpisodesNavItem   downloadedItemNav;
  private NavDivider                  favoriteDividerNav;
  private ForumNavItem                forumNavItem;
  private NavigationListener          listener;
  private ProgramEpisodesFragment     programEpisodesFragment;
  private Program                     currentProgram;
  private Fragment                    currentFragment;

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
    this.playQueueItemNav         = new PlayQueueNavItem(activity);
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
    drawerNavAdapter.add(playQueueItemNav);

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
      currentProgram               = null;
      NavItemFragment itemFragment = (NavItemFragment)drawerNavAdapter.getItem(position);

      if (currentFragment != itemFragment.getFragment()) {
        currentFragment              = itemFragment.getFragment();
        this.listener.onNavigationFragmentSelect(itemFragment.getFragment());
      }
    } else if (drawerNavAdapter.isNavFavoriteProgramItem(position)) {
      FavoriteProgramNavItem itemFragment = (FavoriteProgramNavItem)drawerNavAdapter.getItem(position);
      if (programEpisodesFragment == null) {
        programEpisodesFragment = new ProgramEpisodesFragment();
      }
      currentFragment = null;
      if (currentProgram != itemFragment.getProgram()) {
        currentProgram = itemFragment.getProgram();
        programEpisodesFragment.setProgram(itemFragment.getProgram());
        this.listener.onNavigationFragmentSelect(programEpisodesFragment);
      }
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
