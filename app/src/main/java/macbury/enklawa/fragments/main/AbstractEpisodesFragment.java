package macbury.enklawa.fragments.main;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.List;

import macbury.enklawa.adapters.EpisodesAdapter;
import macbury.enklawa.adapters.EpisodesAdapterListener;
import macbury.enklawa.db.models.Episode;
import macbury.enklawa.db.models.EpisodeFile;
import macbury.enklawa.dialogs.EpisodeAboutDialog;

/**
 * Created by macbury on 14.09.14.
 */
public abstract class AbstractEpisodesFragment extends EnklawaBaseAbstractListFragment implements EpisodesAdapterListener, DialogInterface.OnDismissListener {
  protected static final String TAG     = "AbstractEpisodesFragment";
  protected List<Episode> episodesArray;
  protected EpisodesAdapter episodeAdapter;
  private EpisodeAboutDialog dialog;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.episodeAdapter = new EpisodesAdapter(getActivity());
    this.episodeAdapter.setListener(this);
    this.setListAdapter(episodeAdapter);
  }

  @Override
  public void onSyncPodUpdate() {
    updateEpisodes();
  }

  public void updateEpisodes() {
    this.episodesArray = getEpisodes();
    this.episodeAdapter.set(episodesArray);
    episodeAdapter.notifyDataSetChanged();
  }

  public abstract List<Episode> getEpisodes();

  @Override
  protected void onDownloadUpdate(int progress, EpisodeFile file) {
    episodeAdapter.notifyDataSetChanged();
  }

  @Override
  public void onListItemClick(ListView l, View v, int position, long id) {
    super.onListItemClick(l, v, position, id);
    Episode episode           = episodeAdapter.getItem(position);
    if (dialog != null) {
      dialog.hide();
      dialog = null;
    }
    this.dialog               = new EpisodeAboutDialog(getActivity(), episode);
    dialog.setOnDismissListener(this);
    dialog.show();
  }

  @Override
  public void onPause() {
    super.onPause();
    if (dialog != null) {
      dialog.hide();
      dialog = null;
    }
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

  @Override
  public void onPauseEpisodeDownloadButtonClick(Episode episode) {
    app.services.pausePlayer();
  }

  @Override
  public void onRemoveEpisodeButtonClick(Episode episode) {

  }

  @Override
  public void onPlayEpisodeDownloadButtonClick(Episode episode) {
    Log.v(TAG, "Clicked play download episode: " + episode.name);
    getActivity().startActivity(app.intents.openPlayerForEpisode(episode));
  }



  @Override
  public void onDismiss(DialogInterface dialog) {
    episodeAdapter.notifyDataSetChanged();
  }
}
