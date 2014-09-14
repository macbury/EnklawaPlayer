package macbury.enklawa.fragments.main.episodes;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.List;

import macbury.enklawa.adapters.EpisodesAdapter;
import macbury.enklawa.adapters.EpisodesAdapterListener;
import macbury.enklawa.db.models.Episode;
import macbury.enklawa.db.models.EpisodeFile;
import macbury.enklawa.fragments.main.AbstractEpisodesFragment;
import macbury.enklawa.managers.ApplicationManager;

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
