package macbury.enklawa.managers;

import android.os.Environment;
import android.util.Log;

import java.io.File;

import macbury.enklawa.db.models.Episode;
import macbury.enklawa.db.models.EpisodeFile;

/**
 * Created by macbury on 23.09.14.
 */
public class StorageManager {
  private static final String ENKLAWA_PREFIX = "enklawa.net";
  private static final String TAG            = "StorageManager";

  public StorageManager() {
    Log.i(TAG, "Storage dir is: " + getPodcastStorageDirectory().getAbsolutePath());
  }

  public File getPodcastStorageDirectory() {
    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PODCASTS), ENKLAWA_PREFIX);
    file.mkdirs();
    return file;
  }

  public File getEpisodeFile(EpisodeFile epf) {
    return new File(getPodcastStorageDirectory(), "episode_"+epf.id+".mp3");
  }
}
