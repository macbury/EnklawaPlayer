package macbury.enklawa.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.koushikdutta.ion.builder.Builders;

import macbury.enklawa.api.APIProgram;
import macbury.enklawa.api.APIResponse;
import macbury.enklawa.db.Program;
import macbury.enklawa.db.scopes.ProgramsScope;
import macbury.enklawa.managers.ApplicationManager;

public class SyncPodService extends Service implements FutureCallback<APIResponse>, ProgressCallback {
  private static final String TAG = "SyncPodService";
  private static final String WAKE_LOCK_TAG = "SyncPodWakeLock";
  private static final int SYNC_POD_NOTIFICATION_ID = 666;
  private ApplicationManager app;
  private PowerManager powerManager;
  private PowerManager.WakeLock wakeLock;

  public SyncPodService() {
  }

  @Override
  public IBinder onBind(Intent intent) {
    throw new UnsupportedOperationException("Not yet implemented");
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    Log.d(TAG, "Destroy");
    wakeLock.release();
    Ion.getDefault(this).cancelAll(this);
  }

  @Override
  public void onCreate() {
    super.onCreate();
    Log.d(TAG, "Create");
    this.app          = (ApplicationManager)getApplication();
    this.powerManager = (PowerManager) getSystemService(POWER_SERVICE);
    this.wakeLock     = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, WAKE_LOCK_TAG);
    this.wakeLock.acquire();
    syncPod();
  }

  private void progress(int progress){
    startForeground(SYNC_POD_NOTIFICATION_ID, this.app.notifications.syncPod(progress));
  }

  private void syncPod() {
    progress(0);
    Log.i(TAG, "Syncing pod: "+app.settings.getApiEndpoint());
    Builders.Any.B request = Ion.with(this).load(app.settings.getApiEndpoint());
    if (app.settings.useProxy()) {
      request.proxy(app.settings.getProxyHost(), app.settings.getProxyPort());
    }

    request.noCache().progress(this).as(new TypeToken<APIResponse>() {
    }).setCallback(this);
  }

  @Override
  public void onCompleted(Exception e, APIResponse result) {
    if (e == null) {
      Log.d(TAG, "Downloaded pod data!");
      syncApiResponseWithDB(result);
    } else {
      Log.e(TAG, "Error while downloading pod data!");
      e.printStackTrace();
    }

    stopSelf();
  }

  private void syncApiResponseWithDB(APIResponse result) {
    progress(0);
    ProgramsScope programs = this.app.db.programs;
    for (APIProgram apiProgram : result.programs) {
      if (programs.save(apiProgram)) {
        Log.v(TAG, "Saved: " +apiProgram.name);
      } else {
        Log.e(TAG, "Could not save: " +apiProgram.name);
      }
    }
  }

  @Override
  public void onProgress(long downloaded, long total) {
    progress(Math.round(downloaded / total * 100));
  }
}
