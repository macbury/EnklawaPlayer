package macbury.enklawa.db.scopes;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

import macbury.enklawa.api.APIEpisode;
import macbury.enklawa.api.APIProgram;
import macbury.enklawa.db.models.Episode;
import macbury.enklawa.db.models.EpisodeFile;
import macbury.enklawa.db.models.Program;
import macbury.enklawa.managers.ApplicationManager;

/**
 * Created by macbury on 10.09.14.
 */
public class EpisodesScope extends AbstractScope<Episode> {
  public EpisodesScope(Dao<Episode, Integer> dao) {
    super(dao);
  }

  public Episode buildFromApi(APIEpisode apiObject) {
    Episode episode  = new Episode();
    episode.name     = apiObject.name;
    episode.duration = apiObject.duration;
    episode.image    = apiObject.image;
    episode.link     = apiObject.link;
    episode.mp3      = apiObject.mp3;
    episode.pubDate  = apiObject.pub_date;
    episode.id       = apiObject.id;
    episode.description = apiObject.description;
    return episode;
  }


  public Episode find(APIEpisode apiObject) {
    return find(apiObject.id);
  }

  public List<Episode> latest(long limit) {
    QueryBuilder<Episode, Integer> builder = dao.queryBuilder();
    builder.limit(limit);
    builder.orderBy("pub_date", false);
    try {
      return builder.query();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public List<Episode> allForProgram(Program program) {
    QueryBuilder<Episode, Integer> builder = dao.queryBuilder();
    try {
      return builder.orderBy("id", false).where().in("program_id", program.id).query();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }
}
