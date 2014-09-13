package macbury.enklawa.managers;

import android.content.Intent;

import macbury.enklawa.services.download.DownloadService;
import macbury.enklawa.services.SyncPodService;

/**
 * Created by macbury on 09.09.14.
 */
public class ServiceManager {
  private final ApplicationManager context;

  public ServiceManager(ApplicationManager applicationManager) {
    this.context = applicationManager;
  }

  public void syncPodService() {
    Intent intent = new Intent(context, SyncPodService.class);
    context.startService(intent);
  }

  public void downloadPendingEpisodes() {
    Intent intent = new Intent(context, DownloadService.class);
    context.startService(intent);
  }
}
