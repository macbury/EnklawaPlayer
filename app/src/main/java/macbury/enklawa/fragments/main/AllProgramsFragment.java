package macbury.enklawa.fragments.main;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.etsy.android.grid.StaggeredGridView;

import java.util.List;

import macbury.enklawa.R;
import macbury.enklawa.activities.ProgramEpisodesActivity;
import macbury.enklawa.adapters.ProgramsAdapter;
import macbury.enklawa.db.models.EpisodeFile;
import macbury.enklawa.db.models.Program;
import macbury.enklawa.fragments.main.episodes.NewestEpisodesFragment;
import macbury.enklawa.fragments.main.episodes.ProgramEpisodesFragment;
import macbury.enklawa.managers.ApplicationManager;

/**
 * Created by macbury on 12.09.14.
 */
public class AllProgramsFragment extends Fragment implements AdapterView.OnItemClickListener {
  private List<Program> programsArray;
  private StaggeredGridView gridView;
  private ProgramsAdapter adapter;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view     = inflater.inflate(R.layout.all_programs, container, false);
    this.gridView = (StaggeredGridView) view.findViewById(R.id.grid_view);
    gridView.setOnItemClickListener(this);
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
      this.adapter = new ProgramsAdapter(this.getActivity().getApplicationContext(), programs);
      this.gridView.setAdapter(adapter);
    }
  }

  @Override
  public void onPause() {
    super.onPause();
    getFragmentManager().popBackStack();
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    Program program = adapter.getItem(position);

    getActivity().startActivity(new Intent(getActivity(), ProgramEpisodesActivity.class));
  }
}
