package macbury.enklawa.fragments.main.episodes;

import java.util.ArrayList;
import java.util.List;

import macbury.enklawa.adapters.EpisodesAdapter;
import macbury.enklawa.db.models.EnqueueEpisode;
import macbury.enklawa.db.models.Episode;
import macbury.enklawa.fragments.main.AbstractEpisodesFragment;
import macbury.enklawa.managers.Enklawa;

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
    Enklawa.current().db.queue.deleteByEpisode(episode);
    updateEpisodes();
  }
}