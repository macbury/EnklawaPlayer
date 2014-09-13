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
  public BroadcastsManager broadcasts;

  private static ApplicationManager current;


  @Override
  public void onCreate() {
    super.onCreate();
    this.current       = this;
    this.broadcasts    = new BroadcastsManager(this);
    this.db            = new DatabaseManager(this);
    this.services      = new ServiceManager(this);
    this.intents       = new IntentManager(this);
    this.settings      = new SettingsManager(this);
    this.notifications = new NotificationsManager(this.getApplicationContext());

    this.db.episodeFiles.markDownloadingAsFailed();
    syncIfFirstBoot();
  }

  private void syncIfFirstBoot() {
    if (ApplicationManager.current().db.episodes.count() == 0) {
      services.syncPodService();
    }
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
