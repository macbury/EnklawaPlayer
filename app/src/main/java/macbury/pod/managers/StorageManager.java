package macbury.pod.managers;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;

import macbury.pod.R;
import macbury.pod.db.models.Episode;
import macbury.pod.db.models.EpisodeFile;

/**
 * Created by macbury on 23.09.14.
 */
public class StorageManager {
  private static final String TAG            = "StorageManager";
  private final Context context;
  private final String downloadPrefix;

  public StorageManager(Context context) {
    this.context = context;
    this.downloadPrefix = context.getString(R.string.download_prefix);
    Log.i(TAG, "Storage dir is: " + getPodcastStorageDirectory().getAbsolutePath());

  }

  public File getPodcastStorageDirectory() {
    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PODCASTS), downloadPrefix);
    file.mkdirs();
    return file;
  }

  public File getEpisodeFile(EpisodeFile epf) {
    return new File(getPodcastStorageDirectory(), "episode_"+epf.id+".mp3");
  }

  public Uri getEpisodeUri(Episode episode) {
    if (episode.getFile() != null) {
      File episodeFile = getEpisodeFile(episode.getFile());
      if (episodeFile.exists()) {
        return Uri.fromFile(episodeFile);
      }
    }
    return Uri.parse(episode.mp3);
  }
}
