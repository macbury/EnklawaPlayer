package macbury.pod.fragments.main;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import macbury.pod.R;
import macbury.pod.db.models.EpisodeFile;
import macbury.pod.managers.App;
import macbury.pod.managers.IntentManager;
import macbury.pod.services.SyncPodService;

/**
 * Created by macbury on 12.09.14.
 */
public abstract class EnklawaBaseAbstractListFragment extends Fragment implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
  protected App app;
  protected ListView listView;
  private SwipeRefreshLayout swipeRefreshLayout;
  private BaseAdapter currentAdapter;

  public EnklawaBaseAbstractListFragment() {
    app = App.current();
  }

  private BroadcastReceiver syncRefreshReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      onSyncPodUpdate();
      swipeRefreshLayout.setRefreshing(SyncPodService.isRunning());
    }
  };

  private BroadcastReceiver downloadRefreshReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      int eid        = intent.getIntExtra(IntentManager.EXTRA_EPISODE_FILE_ID, -1);
      EpisodeFile ep = App.current().db.episodeFiles.find(eid);
      onDownloadUpdate(intent.getIntExtra(IntentManager.EXTRA_PROGRESS, 0), ep);
    }
  };

  protected abstract void onSyncPodUpdate();
  protected abstract void onDownloadUpdate(int progress, EpisodeFile file);

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_list_view_with_swipe_to_refresh, null);
    swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_container);
    listView           = (ListView)view.findViewById(R.id.list_view);
    listView.setOnItemClickListener(this);
    view.setBackgroundColor(Color.WHITE);
    swipeRefreshLayout.setOnRefreshListener(this);
    swipeRefreshLayout.setColorSchemeResources(R.color.progress_color_1, R.color.progress_color_3, R.color.progress_color_4, R.color.progress_color_5);
    return view;
  }

  @Override
  public void onResume() {
    listView.setAdapter(currentAdapter);
    super.onResume();
    swipeRefreshLayout.setRefreshing(SyncPodService.isRunning());
    app.broadcasts.podSyncReceiver(this.getActivity(), syncRefreshReceiver);
    app.broadcasts.downloadReceiver(this.getActivity(), downloadRefreshReceiver);
    onSyncPodUpdate();
  }

  @Override
  public void onPause() {
    super.onPause();
    getActivity().unregisterReceiver(syncRefreshReceiver);
    getActivity().unregisterReceiver(downloadRefreshReceiver);
  }

  public void setListAdapter(BaseAdapter adapter) {
    currentAdapter = adapter;
    if (listView != null)
      listView.setAdapter(adapter);
  }


  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    onListItemClick(listView, view, position, id);
  }

  protected void onListItemClick(ListView listView, View view, int position, long id) {

  }

  @Override
  public void onRefresh() {
    App.current().services.syncPodService();
  }
}
