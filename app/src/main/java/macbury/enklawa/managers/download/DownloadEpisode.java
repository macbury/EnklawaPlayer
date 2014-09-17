package macbury.enklawa.managers.download;

import android.content.Context;

import com.koushikdutta.async.future.Future;
import com.koushikdutta.ion.Ion;

import java.io.File;

import macbury.enklawa.db.models.EpisodeFile;
import macbury.enklawa.managers.download.DownloadManager;

/**
 * Created by macbury on 13.09.14.
 */
public class DownloadEpisode {
  private EpisodeFile  episodeFile;
  private Future<File> download;
  public int progress = 0;

  public DownloadEpisode(EpisodeFile episodeFile, DownloadManager manager) {
    this.episodeFile  = episodeFile;
    this.download     = Ion.with(manager.getContext()).load(episodeFile.episode.mp3).progress(manager).write(episodeFile.file(manager.getContext())).setCallback(manager);
  }

  public Future<File> getDownload() {
    return download;
  }

  public EpisodeFile getEpisodeFile() {
    return episodeFile;
  }

  public int getEpisodeFileId() { return episodeFile.id; }

}
