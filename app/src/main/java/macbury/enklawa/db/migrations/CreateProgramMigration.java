package macbury.enklawa.db.migrations;

import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import macbury.enklawa.db.Program;

/**
 * Created by macbury on 09.09.14.
 */
public class CreateProgramMigration extends Migration {
  public CreateProgramMigration() {
    super(0);
  }

  @Override
  public void up(ConnectionSource connectionSource) throws SQLException {
    TableUtils.createTable(connectionSource, Program.class);
  }
}
