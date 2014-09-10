package macbury.enklawa.managers;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.koushikdutta.ion.Ion;

import java.util.Date;

import macbury.enklawa.extensions.DateDeserializer;
import macbury.enklawa.fragments.SettingsFragment;

/**
 * Created by macbury on 09.09.14.
 */
public class ApplicationManager extends Application {
  public DatabaseManager db;
  public ServiceManager services;
  public IntentManager  intents;
  public SettingsManager settings;
  public NotificationsManager notifications;

  private static ApplicationManager current;
  private Ion.Config ionConfig;

  @Override
  public void onCreate() {
    super.onCreate();
    configureION();
    this.current       = this;
    this.db            = new DatabaseManager(this);
    this.services      = new ServiceManager(this);
    this.intents       = new IntentManager(this);
    this.settings      = new SettingsManager(this);
    this.notifications = new NotificationsManager(this.getApplicationContext());
  }

  private void configureION() {
    Gson gson = new GsonBuilder()
              .registerTypeAdapter(Date.class, new DateDeserializer())
              .create();
    ionConfig = Ion.getDefault(this).configure();
    ionConfig.setLogging("ION", Log.VERBOSE);
    ionConfig.setGson(gson);
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
