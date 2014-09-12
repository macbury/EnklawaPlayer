package macbury.enklawa.fragments.main;

import android.app.ListFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import macbury.enklawa.managers.ApplicationManager;

/**
 * Created by macbury on 12.09.14.
 */
public abstract class EnklawaBaseAbstractListFragment extends ListFragment {
  private BroadcastReceiver syncRefreshReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      update();
    }
  };

  protected abstract void update();

  @Override
  public void onResume() {
    super.onResume();
    ApplicationManager.current().broadcasts.podSyncReceiver(this.getActivity(), syncRefreshReceiver);
    update();
  }

  @Override
  public void onPause() {
    super.onPause();
    getActivity().unregisterReceiver(syncRefreshReceiver);
  }

}
