package macbury.enklawa.managers.download;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.CancellationException;

import macbury.enklawa.db.models.Episode;
import macbury.enklawa.db.models.EpisodeFile;
import macbury.enklawa.managers.Enklawa;

/**
 * Created by macbury on 13.09.14.
 */
public class DownloadManager implements ProgressCallback, FutureCallback<File> {
  private static final String TAG = "DownloadManager";
  private final Context context;
  private final ArrayList<EpisodeFile> queue;
  private final Enklawa app;
  private final DownloadManagerListener listener;
  public static DownloadManager current;
  private DownloadEpisode currentDownload;

  public DownloadManager(Context context, DownloadManagerListener listener) {
    this.listener = listener;
    this.context  = context;
    this.app      = Enklawa.current();
    this.queue    = new ArrayList<EpisodeFile>();
    this.current  = this;
  }

  public void push(ArrayList<EpisodeFile> episodeFiles) {
    for (EpisodeFile epf : episodeFiles) {
      if (isRunning() && currentDownload.getEpisodeFileId() == epf.id) {
        Log.v(TAG, "Already downloading episode: "+ epf.episode.name);
      } else if (queue.indexOf(epf) == -1) {
        Log.v(TAG, "Pushed next episode: "+ epf.episode.name);
        queue.add(epf);
      }
    }

    if (!isRunning()) {
      Log.v(TAG, "Starting download!");
      next();
    }
  }

  private void next() {
    if (!canDownload()) {
      Log.v(TAG, "Not on wifi connection, Canceling download");
      currentDownload = null;
      queue.clear();
      finish();
    } else if (queue.size() > 0) {
      EpisodeFile epf = queue.remove(0);
      Log.v(TAG, "Fetching next episode: "+ epf.episode.name);
      app.db.episodeFiles.markAsRunning(epf);
      currentDownload = new DownloadEpisode(epf, this);
      listener.onDownloadStart(currentDownload);
    } else {
      Log.v(TAG, "Queue is clear. Finish!");
      app.db.episodeFiles.markDownloadingAsFailed();
      finish();
    }
  }

  private boolean canDownload() {
    ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo mWifi               = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
    return mWifi.isConnected();
  }

  private void finish() {
    Log.v(TAG, "Finishing...");
    listener.onDownloadManagerFinishedAll();
    current = null;
  }

  public boolean isRunning() {
    return currentDownload != null;
  }

  public void cancelEpisode(Episode episode) {
    if (isRunning() && currentDownload.getEpisodeFile().id == episode.id) {
      Log.v(TAG, "Canceling running episode::" + episode.name);
      currentDownload.getDownload().cancel();
    } else {
      Log.v(TAG, "Canceling episode:" + episode.name);
      queue.remove(episode.getFile());
    }
    app.db.episodeFiles.destroy(episode.getFile());
    Ion.getDefault(context).cancelAll();
  }

  public void cancelEpisodeById(int episodeId) {
    Log.v(TAG, "Canceling episode by id:" + episodeId);
    cancelEpisode(app.db.episodes.find(episodeId));
  }

  public void cancelAll() {
    for (EpisodeFile epf : queue) {
      app.db.episodeFiles.destroy(epf);
    }
    queue.clear();
    if (isRunning()) {
      currentDownload.getDownload().cancel();
    }
  }

  public Context getContext() {
    return context;
  }

  @Override
  public void onProgress(long downloaded, long total) {
    currentDownload.progress = (int) (((float)downloaded/(float)total) * 100);
    if (currentDownload.progress % 5 == 0) {
      listener.onDownloadProgress(currentDownload);
    }
  }

  @Override
  public void onCompleted(Exception e, File result) {
    EpisodeFile epf = currentDownload.getEpisodeFile();
    if (e != null) {
      app.db.episodeFiles.markAsFailed(epf);
      listener.onDownloadFail(currentDownload, e);
      if (CancellationException.class.isInstance(e)) {
        app.db.episodeFiles.destroy(epf);
      } else {
        Log.e(TAG, "Something failed:" +e.toString());
      }
    } else {
      Log.i(TAG, "Downloaded successul!");
      app.db.episodeFiles.markAsSuccess(epf);
      listener.onDownloadSuccess(currentDownload);
    }

    currentDownload = null;
    next();
  }

  public int size() {
    return queue.size();
  }

  public int getProgress() {
    return isRunning() ? currentDownload.progress : 0;
  }
}
