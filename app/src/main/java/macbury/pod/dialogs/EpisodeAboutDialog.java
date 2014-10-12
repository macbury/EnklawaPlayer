package macbury.pod.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import macbury.pod.R;
import macbury.pod.db.models.Episode;
import macbury.pod.db.models.EpisodeFile;
import macbury.pod.managers.App;

/**
 * Created by macbury on 17.09.14.
 */
public class EpisodeAboutDialog extends Dialog implements View.OnClickListener {
  private static final String TAG = "EpisodeAboutDialog";
  private final Episode episode;
  private final Activity activity;
  private TextView     titleTextView;
  private HtmlTextView descriptionTextView;
  private ImageButton  websiteButton;
  private Button goToProgramButton;
  private ImageButton playDownloadButton;
  private ImageButton streamButton;
  private ImageButton deleteFileButton;
  private ImageButton addToPlaylistButton;

  public EpisodeAboutDialog(Activity activity, Episode episode) {
    super(activity);
    this.activity = activity;
    this.episode = episode;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.dialog_episode_details);

    streamButton        = (ImageButton)findViewById(R.id.button_stream);
    playDownloadButton  = (ImageButton)findViewById(R.id.button_download_play);
    deleteFileButton    = (ImageButton)findViewById(R.id.button_trash);
    goToProgramButton   = (Button)findViewById(R.id.button_go_to_program);
    websiteButton       = (ImageButton)findViewById(R.id.button_website);
    titleTextView       = (TextView)findViewById(R.id.episode_title);
    descriptionTextView = (HtmlTextView)findViewById(R.id.episode_description);
    addToPlaylistButton = (ImageButton)findViewById(R.id.button_add_to_playlist);

    titleTextView.setText(episode.name);
    if (episode.description.length() <= 5) {
      descriptionTextView.setHtmlFromString(getContext().getString(R.string.episode_no_description), false);
    } else {
      descriptionTextView.setHtmlFromString(episode.description, false);
    }

    addToPlaylistButton.setOnClickListener(this);
    streamButton.setOnClickListener(this);
    websiteButton.setOnClickListener(this);
    goToProgramButton.setOnClickListener(this);
    playDownloadButton.setOnClickListener(this);
    deleteFileButton.setOnClickListener(this);

    updateUI();
  }

  private void updateUI() {
    EpisodeFile epf = episode.getFile();
    streamButton.setVisibility(View.GONE);
    deleteFileButton.setVisibility(View.GONE);
    if (epf != null && epf.inDownloadQueue()) {
      playDownloadButton.setImageResource(R.drawable.navigation_cancel);
    } else if (epf != null && epf.isDownloadedAndExists()) {
      playDownloadButton.setImageResource(R.drawable.av_play);
      deleteFileButton.setVisibility(View.VISIBLE);
    } else {
      streamButton.setVisibility(View.VISIBLE);
      playDownloadButton.setImageResource(R.drawable.av_download);
    }

    if (App.current().db.queue.have(episode)) {
      addToPlaylistButton.setVisibility(View.GONE);
    } else {
      addToPlaylistButton.setVisibility(View.VISIBLE);
    }
  }

  @Override
  public void onClick(View v) {
    updateUI();
    App app = App.current();
    if (v == websiteButton) {
      activity.startActivity(app.intents.openURL(episode.link));
    } else if (v == goToProgramButton) {
      activity.startActivity(app.intents.activityForProgramEpisodes(episode.program));
    } else if (v == playDownloadButton) {
      downloadOrPlay();
    } else if (v == streamButton) {
      playEpisode();
    } else if (v == deleteFileButton) {
      app.db.episodeFiles.destroy(episode.getFile());
    } else if (v == addToPlaylistButton) {
      addToPlaylist();
    }

    dismiss();
  }

  private void addToPlaylist() {
    App app     = App.current();
    app.db.queue.createFromEpisode(episode);
    Toast.makeText(app, R.string.added_to_playlist, Toast.LENGTH_LONG).show();
  }

  private void playEpisode() {
    activity.startActivity(App.current().intents.openPlayerForEpisode(episode));
  }

  private void downloadOrPlay() {
    App app     = App.current();
    EpisodeFile epf = episode.getFile();

    if (epf != null && epf.inDownloadQueue()) {
      activity.startService(app.intents.cancelEpisodeDownloadService(episode));
    } else if (epf != null && epf.isDownloadedAndExists()) {
      playEpisode();
    } else {
      app.db.episodeFiles.createFromEpisode(episode);
      app.services.downloadPendingEpisodes();
    }
  }

  @Override
  protected void onStop() {
    super.onStop();
  }
}
