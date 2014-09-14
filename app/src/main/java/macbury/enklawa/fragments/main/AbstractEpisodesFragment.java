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
 * Created by macbury on 14.09.14.
 */
public abstract class AbstractEpisodesFragment extends EnklawaBaseAbstractListFragment implements EpisodesAdapterListener {
  protected static final String TAG     = "AbstractEpisodesFragment";
  protected List<Episode> episodesArray;
  protected EpisodesAdapter episodeAdapter;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.episodeAdapter = new EpisodesAdapter(getActivity());
    this.episodeAdapter.setListener(this);
    this.setListAdapter(episodeAdapter);
  }

  @Override
  public void onSyncPodUpdate() {
    this.episodesArray = getEpisodes();
    this.episodeAdapter.set(episodesArray);
  }

  public abstract List<Episode> getEpisodes();

  @Override
  protected void onDownloadUpdate(int progress, EpisodeFile file) {
    episodeAdapter.notifyDataSetChanged();
  }

  @Override
  public void onListItemClick(ListView l, View v, int position, long id) {
    super.onListItemClick(l, v, position, id);
    //onEpisodeClick();
  }

  @Override
  public void onDownloadEpisodeButtonClick(Episode episode) {
    Log.v(TAG, "Clicked download episode: " + episode.name);
    app.db.episodeFiles.createFromEpisode(episode);
    app.services.downloadPendingEpisodes();
  }

  @Override
  public void onCancelEpisodeDownloadButtonClick(Episode episode) {
    Log.v(TAG, "Clicked cancel download episode: " + episode.name);
    getActivity().startService(app.intents.cancelEpisodeDownloadService(episode));
  }
}
