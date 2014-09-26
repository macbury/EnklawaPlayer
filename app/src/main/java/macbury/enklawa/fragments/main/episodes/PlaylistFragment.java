package macbury.enklawa.fragments.main.episodes;

import java.util.ArrayList;
import java.util.List;

import macbury.enklawa.db.models.EnqueueEpisode;
import macbury.enklawa.db.models.Episode;
import macbury.enklawa.db.models.EpisodeFile;
import macbury.enklawa.fragments.main.AbstractEpisodesFragment;

/**
 * Created by macbury on 23.09.14.
 */
public class PlaylistFragment extends AbstractEpisodesFragment {
  @Override
  public List<Episode> getEpisodes() {
    ArrayList<Episode> episodes = new ArrayList<Episode>();
    for(EnqueueEpisode item : app.db.queue.all()) {
      episodes.add(item.episode);
    }
    return episodes;
  }
}