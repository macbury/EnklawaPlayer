package macbury.enklawa.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.koushikdutta.async.http.AsyncHttpRequest;
import com.koushikdutta.async.http.libcore.RawHeaders;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.loader.AsyncHttpRequestFactory;

import java.util.Date;

import macbury.enklawa.R;
import macbury.enklawa.extensions.DateDeserializer;

/**
 * Created by macbury on 09.09.14.
 */
public class SettingsManager {
  public static final String KEY_API_ENDPOINT   = "pref_key_api_endpoint";
  public static final String KEY_SYNC_FREQ      = "pref_key_sync_frequency";
  public static final String KEY_PROXY_ENABLED  = "pref_key_proxy_enabled";
  public static final String KEY_PROXY_HOST     = "pref_key_proxy_host";
  public static final String KEY_PROXY_PORT     = "pref_key_proxy_port";

  public static final String DEFAULT_POD_URL    = "http://enklawa.macbury.ninja";
  public static final int DEFAULT_SYNC_FREQ     = 360;
  public static final int DEFAULT_PROXY_PORT    = 9050;
  public static final String DEFAULT_PROXY_HOST = "127.0.0.1";

  private final SharedPreferences settings;
  private final Context context;

  public SettingsManager(Context context) {
    this.context = context;
    settings     = PreferenceManager.getDefaultSharedPreferences(context);
    PreferenceManager.setDefaultValues(context, R.xml.settings, false);
    update();
  }

  public void update() {
    updateION();
    // onSyncPodUpdate alarms
  }

  private void updateION() {
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(Date.class, new DateDeserializer())
            .create();
    Ion.Config ionConfig = Ion.getDefault(context).configure();
    ionConfig.setLogging("ION", Log.INFO);
    ionConfig.userAgent("Enklawa Pod");
    if (useProxy()) {
      ionConfig.proxy(getProxyHost(), getProxyPort());
    } else {
      ionConfig.disableProxy();
    }

    ionConfig.setGson(gson);
  }

  public String getApiEndpoint() {
    return settings.getString(KEY_API_ENDPOINT, DEFAULT_POD_URL);
  }

  public int getSyncFreq() {
    return settings.getInt(KEY_SYNC_FREQ, 360);
  }
  
  public boolean useProxy() {
    return settings.getBoolean(KEY_PROXY_ENABLED, false);
  }

  public String getProxyHost() {
    return settings.getString(KEY_PROXY_HOST, DEFAULT_PROXY_HOST);
  }

  public int getProxyPort() {
    return Integer.parseInt(settings.getString(KEY_PROXY_PORT, ""));
  }
}
