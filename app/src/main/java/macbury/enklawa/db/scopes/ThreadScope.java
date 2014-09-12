package macbury.enklawa.db.scopes;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;

import macbury.enklawa.api.APIProgram;
import macbury.enklawa.api.APIThread;
import macbury.enklawa.db.models.ForumThread;
import macbury.enklawa.db.models.Program;

/**
 * Created by macbury on 12.09.14.
 */
public class ThreadScope extends AbstractScope<ForumThread> {
  public ThreadScope(Dao<ForumThread, Integer> dao) {
    super(dao);
  }

  public ForumThread buildFromApi(APIThread apiObject) {
    ForumThread thread     = new ForumThread();
    thread.title           = apiObject.title;
    thread.content         = apiObject.content;
    thread.link            = apiObject.link;
    thread.pubDate         = apiObject.pub_date;
    return thread;
  }

  public ForumThread find(APIThread apiObject) {
    QueryBuilder<ForumThread, Integer> builder = dao.queryBuilder();
    try {
      builder.where().eq("link", apiObject.link);
      return builder.queryForFirst();
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }
}
