package macbury.enklawa.db.scopes;

import com.j256.ormlite.dao.Dao;

import macbury.enklawa.api.APIEpisode;
import macbury.enklawa.api.APIProgram;
import macbury.enklawa.db.models.Episode;
import macbury.enklawa.db.models.Program;

/**
 * Created by macbury on 10.09.14.
 */
public class EpisodesScope extends AbstractScope<APIEpisode, Episode> {
  public EpisodesScope(Dao<Episode, Integer> dao) {
    super(dao);
  }

  @Override
  public Episode buildFromApi(APIEpisode apiObject) {
    Episode episode  = new Episode();
    episode.name     = apiObject.name;
    episode.duration = apiObject.duration;
    episode.image    = apiObject.image;
    episode.link     = apiObject.link;
    episode.mp3      = apiObject.mp3;
    episode.pubDate  = apiObject.pub_date;
    episode.id       = apiObject.id;
    return episode;
  }

  @Override
  public Episode find(APIEpisode apiObject) {
    return find(apiObject.id);
  }
}
