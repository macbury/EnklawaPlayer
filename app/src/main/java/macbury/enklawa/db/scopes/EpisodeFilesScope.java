package macbury.enklawa.db.scopes;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import macbury.enklawa.db.models.Episode;
import macbury.enklawa.db.models.EpisodeFile;

/**
 * Created by macbury on 11.09.14.
 */
public class EpisodeFilesScope extends AbstractScope<EpisodeFile> {
  public EpisodeFilesScope(Dao<EpisodeFile, Integer> dao) {
    super(dao);
  }

  public boolean createFromEpisode(Episode episode) {
    EpisodeFile file = findByEpisodeId(episode.id);
    if (file == null) {
      file = new EpisodeFile();
      file.episode     = episode;
      return update(file);
    } else {
      return true;
    }
  }

  public EpisodeFile findByEpisodeId(int id) {
    try {
      return dao.queryBuilder().where().eq("episode_id", id).queryForFirst();
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }
}
