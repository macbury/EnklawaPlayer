package macbury.pod.db.scopes;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import macbury.pod.api.APIEpisode;
import macbury.pod.db.models.Episode;
import macbury.pod.db.models.Program;

/**
 * Created by macbury on 10.09.14.
 */
public class EpisodesScope extends AbstractScope<Episode> {
  public EpisodesScope(Dao<Episode, Integer> dao) {
    super(dao);
  }

  public Episode buildFromApi(APIEpisode apiObject) {
    Episode episode     = new Episode();
    episode.name        = apiObject.name;
    episode.duration    = apiObject.duration;
    episode.image       = apiObject.image;
    episode.link        = apiObject.link;
    episode.mp3         = apiObject.mp3;
    episode.pubDate     = apiObject.pub_date;
    episode.id          = apiObject.id;
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

  @Override
  public void afterCreate(Episode model) {

  }

  @Override
  public void afterDestroy(Episode object) {

  }

  @Override
  public void afterSave(Episode object) {

  }

  public ArrayList<Episode> findEpisodesByApiProgramAndReturnNew(Program program, ArrayList<APIEpisode> rawEpisodes) {
    ArrayList<Integer> nids = new ArrayList<Integer>();
    nids.add(-1);
    for (APIEpisode episode : rawEpisodes) {
      nids.add(episode.id);
    }
    ArrayList<Episode> output              = new ArrayList<Episode>();
    QueryBuilder<Episode, Integer> builder = dao.queryBuilder();

    try {
      ArrayList<Integer> existingEpisodesIds = new ArrayList<Integer>();

      for (Episode episode : builder.where().eq("program_id", program.id).and().in("id", nids).query()) {
        existingEpisodesIds.add(episode.id);
      }

      for (APIEpisode rawEpisode : rawEpisodes) {
        if (existingEpisodesIds.indexOf(rawEpisode.id) == -1) {
          output.add(buildFromApi(rawEpisode));
        }
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return output;
  }
}
