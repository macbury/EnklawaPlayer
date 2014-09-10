package macbury.enklawa.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import macbury.enklawa.R;

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

  public SettingsManager(Context context) {
    settings = PreferenceManager.getDefaultSharedPreferences(context);
    PreferenceManager.setDefaultValues(context, R.xml.settings, false);
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
