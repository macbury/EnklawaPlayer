package macbury.pod.activities.main;


import android.app.Fragment;

import java.util.List;

import macbury.pod.db.models.Program;

/**
 * Created by macbury on 12.09.14.
 */
public interface NavigationListener {

  public void onNavigationFragmentSelect(Fragment fragment);
  public List<Program> getNavigationControllerFavoritedPrograms();

}
