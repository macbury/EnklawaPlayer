package macbury.pod.managers;

import android.app.Application;

/**
 * Created by macbury on 09.09.14.
 */
public class App extends Application {
  public DatabaseManager db;
  public ServiceManager services;
  public IntentManager  intents;
  public SettingsManager settings;
  public NotificationsManager notifications;
  public BroadcastsManager broadcasts;
  public StorageManager storage;
  public AlarmsManager alarms;
  private static App current;


  @Override
  public void onCreate() {
    super.onCreate();
    this.current       = this;
    this.broadcasts    = new BroadcastsManager(this);
    this.db            = new DatabaseManager(this);
    this.services      = new ServiceManager(this);
    this.intents       = new IntentManager(this);
    this.notifications = new NotificationsManager(this.getApplicationContext());
    this.storage       = new StorageManager(this);
    this.settings      = new SettingsManager(this);
    this.alarms        = new AlarmsManager(this);
    this.settings.update();
    this.db.episodeFiles.markDownloadingAsFailed();
    syncIfFirstBoot();
  }

  private void syncIfFirstBoot() {
    if (App.current().db.episodes.count() == 0) {
      services.syncPodService();
    }
  }

  public static synchronized App current() {
    return current;
  }

  @Override
  public void onTerminate() {
    super.onTerminate();
    this.db.close();
    this.current = null;
  }
}
