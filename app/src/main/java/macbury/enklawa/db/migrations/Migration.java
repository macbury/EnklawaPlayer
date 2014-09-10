package macbury.enklawa.db.migrations;

import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

/**
 * Created by macbury on 09.09.14.
 */
public abstract class Migration {

  private final int version;

  public Migration(int version) {
    this.version = version;
  }

  public int getVersion() {
    return this.version;
  }

  public abstract void up(ConnectionSource connectionSource) throws SQLException;
}
