package macbury.enklawa.adapters;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.noveogroup.highlightify.Highlightify;

import java.util.ArrayList;
import java.util.List;

import macbury.enklawa.R;
import macbury.enklawa.db.models.Episode;
import macbury.enklawa.db.models.EpisodeFile;
import macbury.enklawa.extensions.Converter;
import macbury.enklawa.managers.download.DownloadManager;

/**
 * Created by macbury on 12.09.14.
 */
public class EpisodesAdapter extends BaseAdapter implements View.OnClickListener {
  public enum SecondaryAction {
    Download, CancelDownload, Pause, Play, None
  }
  private final Context context;
  private ArrayList<Episode> episodesArray;
  private EpisodesAdapterListener listener;

  public void setListener(EpisodesAdapterListener listener) {
    this.listener = listener;
  }

  public EpisodesAdapter(Context context) {
    super();
    this.context       = context;
    this.episodesArray = new ArrayList<Episode>();
  }

  @Override
  public int getCount() {
    return episodesArray.size();
  }

  @Override
  public Episode getItem(int position) {
    if (position < episodesArray.size() && position != -1){
      return episodesArray.get(position);
    } else {
      return null;
    }
  }

  @Override
  public long getItemId(int position) {
    Episode episode = getItem(position);
    return episode == null ? -1 : episode.id;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    EpisodeHolder holder = null;
    Episode episode      = getItem(position);

    if (convertView == null) {
      holder                      = new EpisodeHolder();
      LayoutInflater inflater     = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      convertView                 = inflater.inflate(R.layout.list_view_episode, parent, false);
      holder.pubDateTextView      = (TextView)convertView.findViewById(R.id.item_episode_published_at);
      holder.titleTextView        = (TextView)convertView.findViewById(R.id.item_episode_title);
      holder.previewImageView     = (ImageView)convertView.findViewById(R.id.item_episode_preview);
      holder.durationTextView     = (TextView)convertView.findViewById(R.id.item_episode_duration);
      holder.secondaryAction      = (ImageButton)convertView.findViewById(R.id.item_episode_button_secondary_action);
      holder.statusTextView       = (TextView)convertView.findViewById(R.id.item_episode_status);
      holder.progressBar          = (ProgressBar) convertView.findViewById(R.id.item_episode_progress);
      holder.container            = convertView;

      holder.secondaryAction.setTag(holder);
      holder.secondaryAction.setOnClickListener(this);

      Highlightify highlightify = new Highlightify();
      highlightify.highlightWithChildren(convertView);

      convertView.setTag(holder);
    } else {
      holder  = (EpisodeHolder)convertView.getTag();
    }

    holder.episode              = episode;
    holder.position             = position;

    holder.titleTextView.setText(episode.name);
    holder.durationTextView.setText(Converter.getDurationStringLong(episode.getDuration()));
    holder.pubDateTextView.setText(DateUtils.formatDateTime(context, episode.pubDate.getTime(), DateUtils.FORMAT_SHOW_DATE));

    EpisodeFile episodeFile = episode.getFile();
    holder.progressBar.setVisibility(View.INVISIBLE);

    if (episodeFile == null || episodeFile.haveFailed()) {
      holder.actionType = SecondaryAction.Download;
      holder.secondaryAction.setImageResource(R.drawable.av_download);
    } else if (episodeFile.isDownloading() || episodeFile.isPending()) {
      holder.actionType = SecondaryAction.CancelDownload;
      holder.secondaryAction.setImageResource(R.drawable.navigation_cancel);
      if (episodeFile.isDownloading()) {
        holder.progressBar.setVisibility(View.VISIBLE);
        if (DownloadManager.current.getProgress() == 0) {
          holder.progressBar.setIndeterminate(true);
        } else {
          holder.progressBar.setIndeterminate(false);
          holder.progressBar.setProgress(DownloadManager.current.getProgress());
        }

      }
    } else {
      holder.actionType = SecondaryAction.Play;
      holder.secondaryAction.setImageResource(R.drawable.av_play);
    }

    if (episode.isUnread()) {
      holder.statusTextView.setText(context.getString(R.string.new_label));
    } else {
      holder.statusTextView.setText("");
    }

    Ion.with(context).load(episode.image).withBitmap().intoImageView(holder.previewImageView);
    return convertView;
  }

  public void set(List<Episode> newEpisodeArray) {
    episodesArray.clear();
    episodesArray.addAll(newEpisodeArray);
    notifyDataSetChanged();
  }

  @Override
  public void onClick(View view) {
    EpisodeHolder holder = (EpisodeHolder) view.getTag();
    if (holder.secondaryAction == view){
      switch (holder.actionType) {
        case Download:
          listener.onDownloadEpisodeButtonClick(holder.episode);
        break;

        case CancelDownload:
          listener.onCancelEpisodeDownloadButtonClick(holder.episode);
        break;
      }

    }
    notifyDataSetChanged();
  }

  public class EpisodeHolder {
    public TextView titleTextView;
    public ImageView previewImageView;
    public View container;
    public TextView pubDateTextView;
    public TextView durationTextView;
    public ImageButton secondaryAction;
    public TextView statusTextView;
    public ProgressBar progressBar;
    public int position;
    public SecondaryAction actionType;
    public Episode episode;
  }
}
