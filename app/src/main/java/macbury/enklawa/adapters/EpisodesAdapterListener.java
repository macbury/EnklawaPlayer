package macbury.enklawa.adapters;

import macbury.enklawa.db.models.Episode;

/**
 * Created by macbury on 13.09.14.
 */
public interface EpisodesAdapterListener {
  public void onDownloadEpisodeButtonClick(Episode episode);
  public void onCancelEpisodeDownloadButtonClick(Episode episode);
  public void onPlayEpisodeDownloadButtonClick(Episode episode);
  public void onPauseEpisodeDownloadButtonClick(Episode episode);
  public void onRemoveEpisodeDownloadButtonClick(Episode episode);
}
