package macbury.enklawa.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import macbury.enklawa.api.APIResponse;
import macbury.enklawa.managers.ApplicationManager;

public class SyncPodService extends Service implements FutureCallback<APIResponse> {
  private static final String TAG = "SyncPodService";
  private ApplicationManager app;

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
    Ion.getDefault(this).cancelAll(this);
  }

  @Override
  public void onCreate() {
    super.onCreate();
    Log.d(TAG, "Create");
    this.app = (ApplicationManager)getApplication();
    Ion.with(this).load(app.settings.getApiEndpoint()).as(new TypeToken<APIResponse>(){}).setCallback(this);
  }

  @Override
  public void onCompleted(Exception e, APIResponse result) {
    Log.d(TAG, "Downloaded!");
    stopSelf();
  }
}
