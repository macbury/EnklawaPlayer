package macbury.pod.managers;

import android.content.Intent;

import macbury.pod.db.models.Episode;
import macbury.pod.db.models.EpisodeFile;
import macbury.pod.services.DownloadService;
import macbury.pod.services.SyncPodService;

/**
 * Created by macbury on 09.09.14.
 */
public class ServiceManager {
  private final App context;

  public ServiceManager(App app) {
    this.context = app;
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

  public void playRadio() {
    context.startService(context.intents.playRadio());
  }
}
