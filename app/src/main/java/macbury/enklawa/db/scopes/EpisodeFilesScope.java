package macbury.enklawa.db.scopes;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.io.File;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import macbury.enklawa.db.models.Episode;
import macbury.enklawa.db.models.EpisodeFile;
import macbury.enklawa.managers.Enklawa;

/**
 * Created by macbury on 11.09.14.
 */
public class EpisodeFilesScope extends AbstractScope<EpisodeFile> {
  private static final String TAG = "EpisodeFilesScope";

  public EpisodeFilesScope(Dao<EpisodeFile, Integer> dao) {
    super(dao);
  }

  public boolean createFromEpisode(Episode episode) {
    EpisodeFile file = findByEpisodeId(episode.id);
    if (file == null) {
      file             = new EpisodeFile();
      file.episode     = episode;
      file.status      = EpisodeFile.Status.Pending;
      file.createdAt   = new Date();
    }
    file.retryCount  = 0;

    return update(file);
  }

  public EpisodeFile findByEpisodeId(int id) {
    try {
      return dao.queryBuilder().where().eq("episode_id", id).queryForFirst();
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public List<EpisodeFile> pending() {
    QueryBuilder<EpisodeFile, Integer> builder = dao.queryBuilder();
    try {
      return builder.where().in("status", EpisodeFile.Status.Failed, EpisodeFile.Status.Pending).and().le("retry_count", EpisodeFile.MAX_RETRY).query();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public List<EpisodeFile> downloading() {
    QueryBuilder<EpisodeFile, Integer> builder = dao.queryBuilder();
    try {
      return builder.where().in("status", EpisodeFile.Status.Downloading).query();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public void markDownloadingAsFailed() {
    for (EpisodeFile file : downloading()) {
      markAsFailed(file);
    }
  }

  public void markAsRunning(EpisodeFile epf) {
    epf.status = EpisodeFile.Status.Downloading;
    update(epf);
  }

  public void markAsFailed(EpisodeFile epf) {
    epf.status        = EpisodeFile.Status.Failed;
    epf.retryCount++;
    update(epf);
  }

  public void markAsSuccess(EpisodeFile epf) {
    epf.status        = EpisodeFile.Status.Ready;
    epf.retryCount    = 0;
    update(epf);
  }

  public List<EpisodeFile> downloadedOrPendingOrFailed() {
    QueryBuilder<EpisodeFile, Integer> builder = dao.queryBuilder();
    try {
      return builder.orderBy("id", true).where().in("status", EpisodeFile.Status.Pending, EpisodeFile.Status.Ready, EpisodeFile.Status.Failed).query();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public int countDownloadedOrPendingOrFailed() {
    QueryBuilder<EpisodeFile, Integer> builder = dao.queryBuilder();
    try {
      return (int)builder.where().in("status", EpisodeFile.Status.Pending, EpisodeFile.Status.Ready, EpisodeFile.Status.Failed).countOf();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return 0;
  }

  @Override
  public void afterCreate(EpisodeFile model) {

  }

  @Override
  public void afterDestroy(EpisodeFile object) {
    Log.i(TAG, "Deleting file for: " + object.episode.name);
    File file = Enklawa.current().storage.getEpisodeFile(object);
    if (file.exists()) {
      file.delete();
    }
  }

  @Override
  public void afterSave(EpisodeFile object) {

  }
}
