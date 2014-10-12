package macbury.pod.fragments.main.episodes;

import java.util.ArrayList;
import java.util.List;

import macbury.pod.adapters.EpisodesAdapter;
import macbury.pod.db.models.EnqueueEpisode;
import macbury.pod.db.models.Episode;
import macbury.pod.fragments.main.AbstractEpisodesFragment;
import macbury.pod.managers.App;

/**
 * Created by macbury on 23.09.14.
 */
public class PlaylistFragment extends AbstractEpisodesFragment {

  @Override
  public void onResume() {
    super.onResume();
    episodeAdapter.setMode(EpisodesAdapter.ActionMode.Trash);
  }

  @Override
  public List<Episode> getEpisodes() {
    ArrayList<Episode> episodes = new ArrayList<Episode>();
    for(EnqueueEpisode item : app.db.queue.pendingToPlay()) {
      episodes.add(item.episode);
    }
    return episodes;
  }

  @Override
  public void onRemoveEpisodeButtonClick(Episode episode) {
    super.onRemoveEpisodeButtonClick(episode);
    App.current().db.queue.deleteByEpisode(episode);
    updateEpisodes();
  }
}