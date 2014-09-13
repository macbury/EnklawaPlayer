package macbury.enklawa.fragments.main;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.List;

import macbury.enklawa.adapters.EpisodesAdapter;
import macbury.enklawa.adapters.EpisodesAdapterListener;
import macbury.enklawa.db.models.Episode;
import macbury.enklawa.db.models.EpisodeFile;
import macbury.enklawa.managers.ApplicationManager;

/**
 * Created by macbury on 12.09.14.
 */
public class NewestEpisodesFragment extends EnklawaBaseAbstractListFragment implements EpisodesAdapterListener {
  private static final long MAX_ITEMS = 16;
  private static final String TAG     = "NewestEpisodesFragment";
  private List<Episode> episodesArray;
  private EpisodesAdapter episodeAdapter;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.episodeAdapter = new EpisodesAdapter(getActivity());
    this.episodeAdapter.setListener(this);
    this.setListAdapter(episodeAdapter);
  }

  @Override
  public void onSyncPodUpdate() {
    this.episodesArray = ApplicationManager.current().db.episodes.latest(MAX_ITEMS);
    this.episodeAdapter.set(episodesArray);
  }

  @Override
  protected void onDownloadUpdate(int progress, EpisodeFile file) {
    //episodeAdapter.updateProgressFor(getListView(), file, progress);
    episodeAdapter.notifyDataSetChanged();
  }

  @Override
  public void onListItemClick(ListView l, View v, int position, long id) {
    super.onListItemClick(l, v, position, id);
  }

  @Override
  public void onDownloadEpisodeButtonClick(Episode episode) {
    Log.i(TAG, "Clicked download episode: " + episode.name);
    app.db.episodeFiles.createFromEpisode(episode);
    app.services.downloadPendingEpisodes();
  }

  @Override
  public void onCancelEpisodeDownloadButtonClick(Episode episode) {
    Log.i(TAG, "Clicked cancel download episode: " + episode.name);
    getActivity().startService(app.intents.cancelEpisodeDownloadService(episode));
  }
}
