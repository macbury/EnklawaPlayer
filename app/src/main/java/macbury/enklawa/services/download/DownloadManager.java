package macbury.enklawa.services.download;

import android.content.Context;
import android.util.Log;

import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.CancellationException;

import macbury.enklawa.db.models.Episode;
import macbury.enklawa.db.models.EpisodeFile;
import macbury.enklawa.managers.ApplicationManager;

/**
 * Created by macbury on 13.09.14.
 */
public class DownloadManager implements ProgressCallback, FutureCallback<File> {
  private static final String TAG = "DownloadManager";
  private final Context context;
  private final ArrayList<EpisodeFile> queue;
  private final ApplicationManager app;
  private final DownloadManagerListener listener;
  private DownloadEpisode currentDownload;

  public DownloadManager(Context context, DownloadManagerListener listener) {
    this.listener = listener;
    this.context  = context;
    this.app      = ApplicationManager.current();
    this.queue    = new ArrayList<EpisodeFile>();
  }

  public void push(ArrayList<EpisodeFile> episodeFiles) {
    for (EpisodeFile epf : episodeFiles) {
      if (queue.indexOf(epf) == -1)
        queue.add(epf);
    }

    if (!isRunning()) {
      next();
    }
  }

  private void next() {
    if (queue.size() > 0) {
      EpisodeFile epf = queue.remove(0);
      app.db.episodeFiles.markAsRunning(epf);
      currentDownload = new DownloadEpisode(epf, this);
      listener.onDownloadStart(currentDownload);
    } else {
      listener.onDownloadManagerFinishedAll();
    }
  }

  private boolean isRunning() {
    return currentDownload != null;
  }

  public void cancelEpisode(Episode episode) {
    if (isRunning() && currentDownload.getEpisodeFile().id == episode.id) {
      currentDownload.getDownload().cancel();
    } else {
      queue.remove(episode.getFile());
    }
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
    float progress = (int) (((float)downloaded/(float)total) * 100);
    if (progress % 5 == 0) {
      listener.onDownloadProgress(currentDownload, progress);
    }
  }

  @Override
  public void onCompleted(Exception e, File result) {
    EpisodeFile epf = currentDownload.getEpisodeFile();
    if (e != null) {
      if (CancellationException.class.isInstance(e)) {
        Log.e(TAG, "Forced cancel");
        app.db.episodeFiles.destroy(epf);
      } else {
        Log.e(TAG, "Something failed:" +e.toString());
        app.db.episodeFiles.markAsFailed(epf);
        listener.onDownloadFail(currentDownload, e);
      }
    } else {
      Log.i(TAG, "Downloaded successul!");
      app.db.episodeFiles.markAsSuccess(epf);
    }

    currentDownload = null;
    next();
  }
}
