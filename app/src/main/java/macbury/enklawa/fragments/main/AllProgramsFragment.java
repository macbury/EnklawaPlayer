package macbury.enklawa.fragments.main;

import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.etsy.android.grid.StaggeredGridView;

import java.util.List;

import macbury.enklawa.R;
import macbury.enklawa.adapters.ProgramsAdapter;
import macbury.enklawa.db.models.EpisodeFile;
import macbury.enklawa.db.models.Program;
import macbury.enklawa.managers.ApplicationManager;

/**
 * Created by macbury on 12.09.14.
 */
public class AllProgramsFragment extends Fragment {
  private List<Program> programsArray;
  private StaggeredGridView gridView;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view     = inflater.inflate(R.layout.all_programs, container, false);
    this.gridView = (StaggeredGridView) view.findViewById(R.id.grid_view);
    return view;
  }

  @Override
  public void onResume() {
    super.onResume();
    loadPrograms();
  }

  private void loadPrograms() {
    List<Program> programs = ApplicationManager.current().db.programs.allOrderedByName();
    if (programs.size() > 0) {
      this.gridView.setAdapter(new ProgramsAdapter(this.getActivity().getApplicationContext(), programs));
    }
  }

  @Override
  public void onPause() {
    super.onPause();
  }
}
