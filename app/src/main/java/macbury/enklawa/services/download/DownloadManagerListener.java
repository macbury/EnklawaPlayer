package macbury.enklawa.services.download;

/**
 * Created by macbury on 13.09.14.
 */
public interface DownloadManagerListener {
  public void onDownloadManagerFinishedAll();
  public void onDownloadProgress(DownloadEpisode currentDownload, float progress);
  public void onDownloadStart(DownloadEpisode currentDownload);
  public void onDownloadFail(DownloadEpisode currentDownload, Exception e);
}
