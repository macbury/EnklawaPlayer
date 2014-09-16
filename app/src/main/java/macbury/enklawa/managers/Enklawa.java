package macbury.enklawa.managers;

import android.app.Application;

/**
 * Created by macbury on 09.09.14.
 */
public class Enklawa extends Application {
  public DatabaseManager db;
  public ServiceManager services;
  public IntentManager  intents;
  public SettingsManager settings;
  public NotificationsManager notifications;
  public BroadcastsManager broadcasts;

  private static Enklawa current;


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
    if (Enklawa.current().db.episodes.count() == 0) {
      services.syncPodService();
    }
  }

  public static synchronized Enklawa current() {
    return current;
  }

  @Override
  public void onTerminate() {
    super.onTerminate();
    this.db.close();
    this.current = null;
  }
}
