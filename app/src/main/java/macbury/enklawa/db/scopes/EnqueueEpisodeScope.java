package macbury.enklawa.db.scopes;

import com.j256.ormlite.dao.Dao;

import macbury.enklawa.db.models.EnqueueEpisode;

/**
 * Created by macbury on 23.09.14.
 */
public class EnqueueEpisodeScope extends AbstractScope<EnqueueEpisode> {
  public EnqueueEpisodeScope(Dao<EnqueueEpisode, Integer> dao) {
    super(dao);
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
