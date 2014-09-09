package macbury.enklawa.managers;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.koushikdutta.ion.Ion;

import macbury.enklawa.fragments.SettingsFragment;

/**
 * Created by macbury on 09.09.14.
 */
public class ApplicationManager extends Application {
  public DatabaseManager db;
  public ServiceManager services;
  public IntentManager  intents;
  public SettingsManager settings;

  private static ApplicationManager current;

  @Override
  public void onCreate() {
    super.onCreate();
    Ion.getDefault(this).configure().setLogging("ION", Log.VERBOSE);

    this.current  = this;
    this.db       = new DatabaseManager(this);
    this.services = new ServiceManager(this);
    this.intents  = new IntentManager(this);
    this.settings = new SettingsManager(this);
  }

  public static synchronized ApplicationManager current() {
    return current;
  }

  @Override
  public void onTerminate() {
    super.onTerminate();
    this.db.close();
    this.current = null;
  }
}
