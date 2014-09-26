package macbury.enklawa.db.scopes;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

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
      update(file);
    }
    return file;
  }

  public EnqueueEpisode findByEpisodeId(int id) {
    try {
      return dao.queryBuilder().where().eq("episode_id", id).queryForFirst();
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
}
