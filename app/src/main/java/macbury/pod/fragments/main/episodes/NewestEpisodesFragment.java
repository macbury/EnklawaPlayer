package macbury.pod.fragments.main.episodes;

import java.util.List;

import macbury.pod.db.models.Episode;
import macbury.pod.fragments.main.AbstractEpisodesFragment;

/**
 * Created by macbury on 12.09.14.
 */
public class NewestEpisodesFragment extends AbstractEpisodesFragment {
  private static final long MAX_ITEMS = 16;
  private static final String TAG     = "NewestEpisodesFragment";

  @Override
  public List<Episode> getEpisodes() {
    return app.db.episodes.latest(MAX_ITEMS);
  }
}
