package macbury.enklawa.services.download;

import android.content.Context;

import com.koushikdutta.async.future.Future;
import com.koushikdutta.ion.Ion;

import java.io.File;

import macbury.enklawa.db.models.EpisodeFile;

/**
 * Created by macbury on 13.09.14.
 */
public class DownloadEpisode {
  private EpisodeFile  episodeFile;
  private Future<File> download;

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

}
