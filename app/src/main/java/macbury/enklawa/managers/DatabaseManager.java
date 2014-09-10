package macbury.enklawa.managers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.koushikdutta.ion.Ion;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import macbury.enklawa.db.Program;
import macbury.enklawa.db.migrations.CreateProgramMigration;
import macbury.enklawa.db.migrations.Migration;
import macbury.enklawa.db.scopes.ProgramsScope;

/**
 * Created by macbury on 09.09.14.
 */
public class DatabaseManager extends OrmLiteSqliteOpenHelper {
  private static final int DATABASE_VERSION = 1;
  private static final String DATABASE_NAME = "enklawa.store";
  private static final String TAG           = "DatabaseManager";
  public ProgramsScope programs;
  private ArrayList<Migration> migrations;

  public DatabaseManager(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
    Log.i(TAG, "Initialized database: " + DATABASE_NAME + " with version " + DATABASE_VERSION);
    buildMigrations();
    try {
      this.programs = new ProgramsScope(this.<Dao<Program, Integer>, Program>getDao(Program.class));
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private void buildMigrations() {
    this.migrations = new ArrayList<Migration>();
    migrations.add(new CreateProgramMigration());
  }

  @Override
  public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
    Log.d(TAG, "Creating database");
    runMigrations(-1, connectionSource);
  }

  @Override
  public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
    Log.d(TAG, "Upgrading database");
    runMigrations(oldVersion, connectionSource);
  }

  private void runMigrations(int version, ConnectionSource connectionSource) {
    Collections.sort(migrations, new Comparator<Migration>() {
      @Override
      public int compare(Migration a, Migration b) {
        if (a.getVersion() < b.getVersion()) {
          return -1;
        } else if (b.getVersion() < a.getVersion()){
          return 1;
        } else {
          return 0;
        }
      }
    });

    for(int i = 0; i < migrations.size(); i++) {
      Migration migration = migrations.get(i);
      if (migration.getVersion() >= version) {
        Log.i(TAG, "Running: " + migration.getClass().getSimpleName());
        try {
          migration.up(connectionSource);
        } catch (SQLException e) {
          e.printStackTrace();
          break;
        }
      } else {
        Log.i(TAG, "Skipping: " + migration.getClass().getSimpleName());
      }

      migrations.clear();
    }
  }
}
