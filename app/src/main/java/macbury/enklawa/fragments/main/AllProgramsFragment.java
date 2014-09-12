package macbury.enklawa.fragments.main;

import android.app.ListFragment;

import java.util.List;

import macbury.enklawa.db.models.Program;
import macbury.enklawa.managers.ApplicationManager;

/**
 * Created by macbury on 12.09.14.
 */
public class AllProgramsFragment extends EnklawaBaseAbstractListFragment {
  private List<Program> programsArray;

  @Override
  protected void update() {
    this.programsArray = ApplicationManager.current().db.programs.all();
  }
}
