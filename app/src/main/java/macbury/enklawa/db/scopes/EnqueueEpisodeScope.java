package macbury.enklawa.db.scopes;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;

import macbury.enklawa.db.models.EnqueueEpisode;
import macbury.enklawa.db.models.Episode;
import macbury.enklawa.db.models.EpisodeFile;

/**
 * Created by macbury on 23.09.14.
 */
public class EnqueueEpisodeScope extends AbstractScope<EnqueueEpisode> {
  public EnqueueEpisodeScope(Dao<EnqueueEpisode, Integer> dao) {
    super(dao);
  }

  public EnqueueEpisode createFromEpisode(Episode episode) {
    EnqueueEpisode file = findByEpisodeId(episode.id);
    if (file == null) {
      file             = new EnqueueEpisode();
      file.episode     = episode;
      file.position    = maximumPosition() + 1;
      update(file);
    }
    return file;
  }


  public int minimumPosition() {
    try {
      EnqueueEpisode ob =  dao.queryBuilder().orderBy("position", true).queryForFirst();
      if (ob == null) {
        return 0;
      } else {
        return ob.position;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return 0;
  }

  public int maximumPosition() {
    try {
      EnqueueEpisode ob =  dao.queryBuilder().orderBy("position", false).queryForFirst();
      if (ob == null) {
        return 0;
      } else {
        return ob.position;
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return 0;
  }

  public EnqueueEpisode findByEpisodeId(int id) {
    try {
      return dao.queryBuilder().where().eq("episode_id", id).queryForFirst();
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public EnqueueEpisode nextToPlay() {
    try {
      return dao.queryBuilder().orderBy("position", true).queryForFirst();
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public void afterCreate(EnqueueEpisode model) {

  }

  @Override
  public void afterDestroy(EnqueueEpisode object) {

  }

  @Override
  public void afterSave(EnqueueEpisode object) {

  }

  public void moveToBegining(EnqueueEpisode enqueueEpisode) {
    enqueueEpisode.position       = minimumPosition() - 1;
    update(enqueueEpisode);
  }

  public boolean have(Episode episode) {
    return findByEpisodeId(episode.id) != null;
  }

  public void deleteByEpisode(Episode episode) {
    EnqueueEpisode ee = findByEpisodeId(episode.id);
    if (ee != null) {
      destroy(ee);
    }
  }

  public ArrayList<EnqueueEpisode> pendingToPlay() {
    try {
      return new ArrayList<EnqueueEpisode>(dao.queryBuilder().orderBy("position", true).query());
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }
}
