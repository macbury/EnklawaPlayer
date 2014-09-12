package macbury.enklawa.fragments.main;

import android.app.ListFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.List;

import macbury.enklawa.adapters.EpisodesAdapter;
import macbury.enklawa.db.models.Episode;
import macbury.enklawa.managers.ApplicationManager;

/**
 * Created by macbury on 12.09.14.
 */
public class NewestEpisodesFragment extends EnklawaBaseAbstractListFragment {
  private static final long MAX_ITEMS = 16;
  private List<Episode> episodesArray;
  private EpisodesAdapter episodeAdapter;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.episodeAdapter = new EpisodesAdapter(getActivity());
  }

  @Override
  public void update() {
    this.episodesArray = ApplicationManager.current().db.episodes.latest(MAX_ITEMS);
    this.episodeAdapter.set(episodesArray);
    if (episodesArray.size() > 0) {
      this.setListAdapter(episodeAdapter);
    }
  }

  @Override
  public void onListItemClick(ListView l, View v, int position, long id) {
    super.onListItemClick(l, v, position, id);
  }
}
