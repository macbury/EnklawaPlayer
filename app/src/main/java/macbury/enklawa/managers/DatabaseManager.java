package macbury.enklawa.managers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.koushikdutta.ion.Ion;

/**
 * Created by macbury on 09.09.14.
 */
public class DatabaseManager extends OrmLiteSqliteOpenHelper {
  private static final int DATABASE_VERSION = 1;
  private static final String DATABASE_NAME = "enklawa.pod";
  private static final String TAG           = "DatabaseManager";

  public DatabaseManager(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
    Log.i(TAG, "Initialized database: " + DATABASE_NAME + " with version " + DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
    Log.d(TAG, "Creating database");
  }

  @Override
  public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
    Log.d(TAG, "Upgrading database");
  }
}
