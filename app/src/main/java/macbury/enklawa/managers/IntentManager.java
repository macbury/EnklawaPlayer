package macbury.enklawa.managers;

import android.app.PendingIntent;
import android.content.Intent;

import macbury.enklawa.activities.SettingsActivity;
import macbury.enklawa.db.models.Episode;
import macbury.enklawa.db.models.EpisodeFile;
import macbury.enklawa.services.download.DownloadEpisode;
import macbury.enklawa.services.download.DownloadService;

/**
 * Created by macbury on 09.09.14.
 */
public class IntentManager {
  public static final String EXTRA_ACTION_CANCEL           = "EXTRA_ACTION_CANCEL";
  public static final String EXTRA_PROGRESS                = "EXTRA_PROGRESS";
  public static final String EXTRA_EPISODE_FILE_ID         = "EXTRA_EPISODE_FILE_ID";
  public static final String EXTRA_EPISODE                 = "EXTRA_EPISODE";

  private final ApplicationManager context;

  public IntentManager(ApplicationManager applicationManager) {
    this.context = applicationManager;
  }

  public Intent showSettingsActivity() {
    Intent intent = new Intent(context, SettingsActivity.class);
    return intent;
  }

  public PendingIntent cancelDownloadService() {
    Intent intent         = new Intent(context, DownloadService.class);
    intent.putExtra(EXTRA_ACTION_CANCEL, true);
    return PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
  }

  public Intent cancelEpisodeDownloadService(Episode episode) {
    Intent intent         = new Intent(context, DownloadService.class);
    intent.putExtra(EXTRA_ACTION_CANCEL, true);
    intent.putExtra(EXTRA_EPISODE, episode.id);
    return intent;
  }

  public boolean haveCancelExtra(Intent intent){
    return intent.hasExtra(EXTRA_ACTION_CANCEL) && intent.getBooleanExtra(EXTRA_ACTION_CANCEL, false) == true;
  }

  public boolean haveEpisode(Intent intent) {
    return intent.hasExtra(EXTRA_EPISODE);
  }

  public int getEpisodeId(Intent intent) {
    return intent.getIntExtra(EXTRA_EPISODE, -1);
  }

  public Intent downloadStatus(DownloadEpisode download) {
    Intent intent = new Intent(BroadcastsManager.BROADCAST_ACTION_DOWNLOADING);
    intent.putExtra(EXTRA_EPISODE_FILE_ID, download.getEpisodeFile().id);
    intent.putExtra(EXTRA_PROGRESS, download.progress);
    return intent;
  }
}
