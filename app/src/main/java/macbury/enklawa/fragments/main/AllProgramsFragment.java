package macbury.enklawa.fragments.main;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.etsy.android.grid.StaggeredGridView;

import java.util.List;

import macbury.enklawa.R;
import macbury.enklawa.adapters.ProgramsAdapter;
import macbury.enklawa.db.models.Program;
import macbury.enklawa.managers.Enklawa;

/**
 * Created by macbury on 12.09.14.
 */
public class AllProgramsFragment extends Fragment implements ProgramsAdapter.ProgramsAdapterListener {
  private List<Program> programsArray;
  private StaggeredGridView gridView;
  private ProgramsAdapter adapter;

  private BroadcastReceiver syncRefreshReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      onSyncPodUpdate();
    }
  };

  private void onSyncPodUpdate() {
    loadPrograms();
  }

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
    Enklawa.current().broadcasts.podSyncReceiver(this.getActivity(), syncRefreshReceiver);
  }

  private void loadPrograms() {
    List<Program> programs = Enklawa.current().db.programs.allOrderedByName();
    if (programs.size() > 0) {
      this.adapter = new ProgramsAdapter(this.getActivity().getApplicationContext(), programs, this);
      this.gridView.setAdapter(adapter);
    }
  }

  @Override
  public void onPause() {
    super.onPause();
    getFragmentManager().popBackStack();
    getActivity().unregisterReceiver(syncRefreshReceiver);
  }


  @Override
  public void onProgramSelect(Program program) {
    getActivity().startActivity(Enklawa.current().intents.activityForProgramEpisodes(program));
  }
}
