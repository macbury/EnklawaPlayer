package macbury.pod.fragments.main.episodes;

import java.util.ArrayList;
import java.util.List;

import macbury.pod.db.models.Episode;
import macbury.pod.db.models.EpisodeFile;
import macbury.pod.fragments.main.AbstractEpisodesFragment;

/**
 * Created by macbury on 14.09.14.
 */
public class DownloadedEpisodesFragment extends AbstractEpisodesFragment {
  @Override
  public List<Episode> getEpisodes() {
    ArrayList<Episode> episodes = new ArrayList<Episode>();
    for(EpisodeFile epf : app.db.episodeFiles.all()) {
      episodes.add(epf.episode);
    }
    return episodes;
  }


}
