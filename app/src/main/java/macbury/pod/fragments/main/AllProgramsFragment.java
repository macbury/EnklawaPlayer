package macbury.pod.fragments.main;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.etsy.android.grid.StaggeredGridView;

import java.util.List;

import macbury.pod.R;
import macbury.pod.adapters.ProgramsAdapter;
import macbury.pod.db.models.Program;
import macbury.pod.managers.App;
import macbury.pod.services.SyncPodService;

/**
 * Created by macbury on 12.09.14.
 */
public class AllProgramsFragment extends Fragment implements ProgramsAdapter.ProgramsAdapterListener, SwipeRefreshLayout.OnRefreshListener {
  private List<Program> programsArray;
  private StaggeredGridView gridView;
  private ProgramsAdapter adapter;
  private SwipeRefreshLayout swipeRefreshLayout;

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
    View view               = inflater.inflate(R.layout.all_programs, container, false);
    this.swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_container);
    this.gridView           = (StaggeredGridView) view.findViewById(R.id.grid_view);

    swipeRefreshLayout.setOnRefreshListener(this);
    swipeRefreshLayout.setColorSchemeResources(R.color.progress_color_1, R.color.progress_color_3, R.color.progress_color_4, R.color.progress_color_5);
    return view;
  }

  @Override
  public void onResume() {
    super.onResume();
    loadPrograms();
    App.current().broadcasts.podSyncReceiver(this.getActivity(), syncRefreshReceiver);
  }

  private void loadPrograms() {
    swipeRefreshLayout.setRefreshing(SyncPodService.isRunning());
    List<Program> programs = App.current().db.programs.allOrderedByName();
    if (adapter == null) {
      this.adapter = new ProgramsAdapter(this.getActivity().getApplicationContext(), programs, this);
    } else {
      adapter.set(programs);
    }
    this.gridView.setAdapter(adapter);
  }

  @Override
  public void onPause() {
    super.onPause();
    getFragmentManager().popBackStack();
    getActivity().unregisterReceiver(syncRefreshReceiver);
  }


  @Override
  public void onProgramSelect(Program program) {
    getActivity().startActivity(App.current().intents.activityForProgramEpisodes(program));
  }

  @Override
  public void onRefresh() {
    App.current().services.syncPodService();
  }
}
