package macbury.pod.managers.download;

import com.koushikdutta.async.future.Future;
import com.koushikdutta.ion.Ion;

import java.io.File;

import macbury.pod.db.models.Episode;
import macbury.pod.db.models.EpisodeFile;
import macbury.pod.managers.App;

/**
 * Created by macbury on 13.09.14.
 */
public class DownloadEpisode {
  private EpisodeFile  episodeFile;
  private Future<File> download;
  public int progress = 0;

  public DownloadEpisode(EpisodeFile episodeFile, DownloadManager manager) {
    this.episodeFile  = episodeFile;
    this.download     = Ion.with(manager.getContext())
            .load(episodeFile.episode.mp3)
            .progress(manager)
            .write(App.current().storage.getEpisodeFile(episodeFile))
            .setCallback(manager);
  }

  public Future<File> getDownload() {
    return download;
  }

  public EpisodeFile getEpisodeFile() {
    return episodeFile;
  }

  public int getEpisodeFileId() { return episodeFile.id; }

  public Episode getEpisode() {
    return episodeFile.episode;
  }
}
