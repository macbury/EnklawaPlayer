package macbury.enklawa.fragments.main;

import android.app.ListFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import macbury.enklawa.db.models.EpisodeFile;
import macbury.enklawa.managers.Enklawa;
import macbury.enklawa.managers.IntentManager;

/**
 * Created by macbury on 12.09.14.
 */
public abstract class EnklawaBaseAbstractListFragment extends ListFragment {
  protected Enklawa app;

  public EnklawaBaseAbstractListFragment() {
    app = Enklawa.current();
  }

  private BroadcastReceiver syncRefreshReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      onSyncPodUpdate();
    }
  };

  private BroadcastReceiver downloadRefreshReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      int eid        = intent.getIntExtra(IntentManager.EXTRA_EPISODE_FILE_ID, -1);
      EpisodeFile ep = Enklawa.current().db.episodeFiles.find(eid);
      onDownloadUpdate(intent.getIntExtra(IntentManager.EXTRA_PROGRESS, 0), ep);
    }
  };

  protected abstract void onSyncPodUpdate();
  protected abstract void onDownloadUpdate(int progress, EpisodeFile file);

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = super.onCreateView(inflater, container, savedInstanceState);
    view.setBackgroundColor(Color.WHITE);
    return view;
  }

  @Override
  public void onResume() {
    super.onResume();
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

}
