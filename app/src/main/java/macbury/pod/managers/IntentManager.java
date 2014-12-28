package macbury.pod.managers;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;

import macbury.pod.activities.player.PlayerActivity;
import macbury.pod.activities.ProgramEpisodesActivity;
import macbury.pod.activities.SettingsActivity;
import macbury.pod.activities.player.RadioActivity;
import macbury.pod.db.models.Episode;
import macbury.pod.db.models.EpisodeFile;
import macbury.pod.db.models.Program;
import macbury.pod.managers.download.DownloadEpisode;
import macbury.pod.services.DownloadService;
import macbury.pod.services.PlayerService;
import macbury.pod.services.SyncPodService;

/**
 * Created by macbury on 09.09.14.
 */
public class IntentManager {
  public static final String EXTRA_ACTION_KEYCODE          = "EXTRA_ACTION_KEYCODE";
  public static final String EXTRA_ACTION_RADIO            = "EXTRA_ACTION_RADIO";
  public static final String EXTRA_ACTION_PENDING          = "EXTRA_ACTION_PENDING";
  public static final String EXTRA_ACTION_PLAY             = "EXTRA_ACTION_PLAY";
  public static final String EXTRA_ACTION_PAUSE            = "EXTRA_ACTION_PAUSE";
  public static final String EXTRA_ACTION_CANCEL           = "EXTRA_ACTION_CANCEL";
  public static final String EXTRA_PROGRESS                = "EXTRA_PROGRESS";
  public static final String EXTRA_EPISODE_FILE_ID         = "EXTRA_EPISODE_FILE_ID";
  public static final String EXTRA_EPISODE                 = "EXTRA_EPISODE";
  public static final String EXTRA_PROGRAM_ID              = "EXTRA_PROGRAM_ID";

  private final App context;

  public IntentManager(App app) {
    this.context = app;
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
    return intent != null && intent.hasExtra(EXTRA_ACTION_CANCEL) && intent.getBooleanExtra(EXTRA_ACTION_CANCEL, false) == true;
  }

  public PendingIntent syncPod() {
    Intent intent         = new Intent(context, SyncPodService.class);
    return PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
  }

  public boolean haveKeyCode(Intent intent) {
    return intent != null && intent.hasExtra(EXTRA_ACTION_KEYCODE);
  }

  public boolean havePauseExtra(Intent intent) {
    return intent != null && intent.hasExtra(EXTRA_ACTION_PAUSE);
  }

  public boolean haveEpisode(Intent intent) {
    return intent != null && intent.hasExtra(EXTRA_EPISODE);
  }

  public boolean haveProgram(Intent intent) {
    return intent != null && intent.hasExtra(EXTRA_PROGRAM_ID);
  }

  public int getKeyCode(Intent intent) {
    return intent.getIntExtra(EXTRA_ACTION_KEYCODE, -1);
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

  public Intent playRadio() {
    Intent intent = new Intent(context, PlayerService.class);
    intent.putExtra(EXTRA_ACTION_RADIO, true);
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

  public PendingIntent downloadPendingEpisodes() {
    Intent intent         = new Intent(context, DownloadService.class);
    return PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
  }

  public PendingIntent pendingPlayEpisodeFile(EpisodeFile file) {
    return PendingIntent.getService(context, 0, playEpisodeFile(file), PendingIntent.FLAG_UPDATE_CURRENT);
  }

  public PendingIntent pendingOpenPlayerForEpisode(Episode episode) {
    Intent intent = openPlayerForEpisode(episode);
    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
    return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
  }

  public Intent player() {
    return new Intent(context, PlayerService.class);
  }

  public PendingIntent pendingPlayPlayer() {
    Intent intent = player();
    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    return PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
  }

  public PendingIntent pendingPausePlayer() {
    Intent intent = pausePlayer();
    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    return PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
  }

  public PendingIntent pendingStopPlayer() {
    Intent intent = stopPlayer();
    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    return PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
  }

  public Intent playerHandleRemoteControlClient(int keycode) {
    Intent intent = player();
    intent.putExtra(EXTRA_ACTION_KEYCODE, keycode);
    return intent;
  }

  public Intent playEpisode(Episode episode) {
    Intent intent = player();
    intent.putExtra(EXTRA_EPISODE, episode.id);
    return intent;
  }

  public PendingIntent pendingPlayEpisode(Episode episode) {
    return PendingIntent.getService(context, 0, playEpisode(episode), PendingIntent.FLAG_UPDATE_CURRENT);
  }

  public boolean haveRadioExtra(Intent intent) {
    return intent.hasExtra(EXTRA_ACTION_RADIO);
  }

  public Intent playRadioActivity() {
    Intent intent = new Intent(context, RadioActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    return intent;
  }
}
