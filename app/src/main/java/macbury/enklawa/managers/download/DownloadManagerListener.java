package macbury.enklawa.managers.download;

/**
 * Created by macbury on 13.09.14.
 */
public interface DownloadManagerListener {
  public void onDownloadManagerFinishedAll();
  public void onDownloadProgress(DownloadEpisode currentDownload);
  public void onDownloadStart(DownloadEpisode currentDownload);
  public void onDownloadFail(DownloadEpisode currentDownload, Exception e);

  public void onDownloadSuccess(DownloadEpisode currentDownload);
}
