package macbury.enklawa.managers;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;

import macbury.enklawa.activities.PlayerActivity;
import macbury.enklawa.activities.ProgramEpisodesActivity;
import macbury.enklawa.activities.SettingsActivity;
import macbury.enklawa.db.models.Episode;
import macbury.enklawa.db.models.EpisodeFile;
import macbury.enklawa.db.models.Program;
import macbury.enklawa.managers.download.DownloadEpisode;
import macbury.enklawa.services.DownloadService;
import macbury.enklawa.services.PlayerService;

/**
 * Created by macbury on 09.09.14.
 */
public class IntentManager {
  public static final String EXTRA_ACTION_PAUSE            = "EXTRA_ACTION_PAUSE";
  public static final String EXTRA_ACTION_CANCEL           = "EXTRA_ACTION_CANCEL";
  public static final String EXTRA_PROGRESS                = "EXTRA_PROGRESS";
  public static final String EXTRA_EPISODE_FILE_ID         = "EXTRA_EPISODE_FILE_ID";
  public static final String EXTRA_EPISODE                 = "EXTRA_EPISODE";
  public static final String EXTRA_PROGRAM_ID              = "EXTRA_PROGRAM_ID";

  private final Enklawa context;

  public IntentManager(Enklawa enklawa) {
    this.context = enklawa;
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

  public boolean haveProgram(Intent intent) {
    return intent.hasExtra(EXTRA_PROGRAM_ID);
  }

  public int getProgramId(Intent intent) {
    return intent.getIntExtra(EXTRA_PROGRAM_ID, -1);
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

  public Intent activityForProgramEpisodes(Program program) {
    Intent intent         = new Intent(context, ProgramEpisodesActivity.class);
    intent.putExtra(EXTRA_PROGRAM_ID, program.id);
    return intent;
  }

  public Intent favoriteProgram(Program program) {
    Intent intent         = new Intent(BroadcastsManager.BROADCAST_FAVORITE_PROGRAM);
    intent.putExtra(EXTRA_PROGRAM_ID, program.id);
    return intent;
  }

  public Intent openURL(String link) {
    return new Intent(Intent.ACTION_VIEW).setData(Uri.parse(link));
  }

  public Intent openPlayerForEpisode(Episode episode) {
    Intent intent         = new Intent(context, PlayerActivity.class);
    intent.putExtra(EXTRA_EPISODE, episode.id);
    return intent;
  }

  public Intent shareUrl(String link) {
    Intent sendIntent = new Intent();
    sendIntent.setAction(Intent.ACTION_SEND);
    sendIntent.putExtra(Intent.EXTRA_TEXT, link);
    sendIntent.setType("text/plain");
    return sendIntent;
  }

  public Intent playEpisodeFile(EpisodeFile epf) {
    Intent intent = new Intent(context, PlayerService.class);
    intent.putExtra(EXTRA_EPISODE_FILE_ID, epf.id);
    return intent;
  }

  public Intent playEpisodeStream(Episode episode) {
    Intent intent = new Intent(context, PlayerService.class);
    intent.putExtra(EXTRA_EPISODE, episode.id);
    return intent;
  }

  public Intent pausePlayer() {
    Intent intent = new Intent(context, PlayerService.class);
    intent.putExtra(EXTRA_ACTION_PAUSE, true);
    return intent;
  }

  public Intent stopPlayer() {
    Intent intent = new Intent(context, PlayerService.class);
    intent.putExtra(EXTRA_ACTION_CANCEL, true);
    return intent;
  }
}
