package macbury.enklawa.managers;

import android.content.Intent;

import macbury.enklawa.db.models.Episode;
import macbury.enklawa.db.models.EpisodeFile;
import macbury.enklawa.services.DownloadService;
import macbury.enklawa.services.SyncPodService;

/**
 * Created by macbury on 09.09.14.
 */
public class ServiceManager {
  private final Enklawa context;

  public ServiceManager(Enklawa enklawa) {
    this.context = enklawa;
  }

  public void syncPodService() {
    Intent intent = new Intent(context, SyncPodService.class);
    context.startService(intent);
  }

  public void downloadPendingEpisodes() {
    Intent intent = new Intent(context, DownloadService.class);
    context.startService(intent);
  }

  public void playEpisodeStream(Episode episode) {
    context.startService(context.intents.playEpisodeStream(episode));
  }

  public void playEpisodeFile(EpisodeFile epf) {
    context.startService(context.intents.playEpisodeFile(epf));
  }

  public void pausePlayer() {
    context.startService(context.intents.pausePlayer());
  }

  public void playNextInQueue() {
    context.startService(context.intents.player());
  }

  public void playEpisode(Episode episode) {
    context.startService(context.intents.playEpisode(episode));
  }

  public void player() {
    context.startService(context.intents.player());
  }

  public void stopPlayer() {
    context.startService(context.intents.stopPlayer());
  }
}
