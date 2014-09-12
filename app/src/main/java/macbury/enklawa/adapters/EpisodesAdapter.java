package macbury.enklawa.adapters;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

import macbury.enklawa.R;
import macbury.enklawa.db.models.Episode;
import macbury.enklawa.db.models.Program;
import macbury.enklawa.extensions.Converter;

/**
 * Created by macbury on 12.09.14.
 */
public class EpisodesAdapter extends BaseAdapter {
  private final Context context;
  private ArrayList<Episode> episodesArray;

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
    return episodesArray.get(position);
  }

  @Override
  public long getItemId(int position) {
    return getItem(position).id;
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
      holder.secondaryAction      = (ImageView)convertView.findViewById(R.id.item_episode_button_secondary_action);
      holder.statusTextView       = (TextView)convertView.findViewById(R.id.item_episode_status);
      holder.progressBar          = (ProgressBar) convertView.findViewById(R.id.item_episode_progress);
      holder.container            = convertView;
      convertView.setTag(holder);
    } else {
      holder  = (EpisodeHolder)convertView.getTag();
    }

    holder.titleTextView.setText(episode.name);
    holder.durationTextView.setText(Converter.getDurationStringShort(episode.duration));
    holder.pubDateTextView.setText(DateUtils.formatDateTime(context, episode.pubDate.getTime(), DateUtils.FORMAT_SHOW_DATE));

    if (episode.isDownloaded()) {
      holder.secondaryAction.setImageResource(R.drawable.ic_action_av_play);
    } else {
      holder.secondaryAction.setImageResource(R.drawable.ic_action_av_download);
    }

    if (episode.isUnread()) {
      holder.statusTextView.setText(context.getString(R.string.new_label));
      holder.progressBar.setVisibility(View.INVISIBLE);
    } else {
      holder.progressBar.setVisibility(View.VISIBLE);
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

  public class EpisodeHolder {
    public TextView titleTextView;
    public ImageView previewImageView;
    public View container;
    public TextView pubDateTextView;
    public TextView durationTextView;
    public ImageView secondaryAction;
    public TextView statusTextView;
    public ProgressBar progressBar;
  }
}
