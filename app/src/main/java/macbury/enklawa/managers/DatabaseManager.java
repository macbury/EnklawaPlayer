package macbury.enklawa.managers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import macbury.enklawa.db.models.Episode;
import macbury.enklawa.db.models.EpisodeFile;
import macbury.enklawa.db.models.Program;
import macbury.enklawa.db.scopes.EpisodeFilesScope;
import macbury.enklawa.db.scopes.EpisodesScope;
import macbury.enklawa.db.scopes.ProgramsScope;

/**
 * Created by macbury on 09.09.14.
 */
public class DatabaseManager extends OrmLiteSqliteOpenHelper {
  private static final int DATABASE_VERSION = 31;
  private static final String DATABASE_NAME = "pods.db";
  private static final String TAG           = "DatabaseManager";
  public EpisodeFilesScope episodeFiles;
  public EpisodesScope episodes;
  public ProgramsScope programs;

  public DatabaseManager(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
    Log.i(TAG, "Initialized database: " + DATABASE_NAME + " with version " + DATABASE_VERSION);

    try {
      this.programs     = new ProgramsScope(this.<Dao<Program, Integer>, Program>getDao(Program.class));
      this.episodes     = new EpisodesScope(this.<Dao<Episode, Integer>, Episode>getDao(Episode.class));
      this.episodeFiles = new EpisodeFilesScope(this.<Dao<EpisodeFile, Integer>, EpisodeFile>getDao(EpisodeFile.class));
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
    Log.d(TAG, "Creating database");
    createTables();
  }

  private void createTables() {
    try {
      TableUtils.createTable(connectionSource, Program.class);
      TableUtils.createTable(connectionSource, Episode.class);
      TableUtils.createTable(connectionSource, EpisodeFile.class);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
    Log.d(TAG, "Upgrading database");

    try {
      TableUtils.dropTable(connectionSource, Program.class, true);
      TableUtils.dropTable(connectionSource, Episode.class, true);
      TableUtils.dropTable(connectionSource, EpisodeFile.class, true);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    createTables();
  }

}
