package macbury.enklawa.fragments.main;

import android.app.ListFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import macbury.enklawa.db.models.EpisodeFile;
import macbury.enklawa.managers.ApplicationManager;
import macbury.enklawa.managers.IntentManager;

/**
 * Created by macbury on 12.09.14.
 */
public abstract class EnklawaBaseAbstractListFragment extends ListFragment {
  protected ApplicationManager app;

  public EnklawaBaseAbstractListFragment() {
    app = ApplicationManager.current();
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
      EpisodeFile ep = ApplicationManager.current().db.episodeFiles.find(eid);
      onDownloadUpdate(intent.getIntExtra(IntentManager.EXTRA_PROGRESS, 0), ep);
    }
  };

  protected abstract void onSyncPodUpdate();
  protected abstract void onDownloadUpdate(int progress, EpisodeFile file);

  @Override
  public void onResume() {
    super.onResume();
    ApplicationManager.current().broadcasts.podSyncReceiver(this.getActivity(), syncRefreshReceiver);
    ApplicationManager.current().broadcasts.downloadReceiver(this.getActivity(), downloadRefreshReceiver);
    onSyncPodUpdate();
  }

  @Override
  public void onPause() {
    super.onPause();
    getActivity().unregisterReceiver(syncRefreshReceiver);
    getActivity().unregisterReceiver(downloadRefreshReceiver);
  }

}
