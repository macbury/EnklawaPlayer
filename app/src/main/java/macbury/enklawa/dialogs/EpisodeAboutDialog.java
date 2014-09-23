package macbury.enklawa.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import macbury.enklawa.R;
import macbury.enklawa.db.models.Episode;
import macbury.enklawa.db.models.EpisodeFile;
import macbury.enklawa.managers.Enklawa;

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

    titleTextView.setText(episode.name);
    if (episode.description.length() <= 5) {
      descriptionTextView.setHtmlFromString(getContext().getString(R.string.episode_no_description), false);
    } else {
      descriptionTextView.setHtmlFromString(episode.description, false);
    }

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
  }

  @Override
  public void onClick(View v) {
    updateUI();
    Enklawa app = Enklawa.current();
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
    }

    dismiss();
  }

  private void playEpisode() {
    activity.startActivity(Enklawa.current().intents.openPlayerForEpisode(episode));
  }

  private void downloadOrPlay() {
    Enklawa app     = Enklawa.current();
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
