package macbury.enklawa.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by macbury on 09.09.14.
 */
public class SettingsManager {
  public static final String KEY_API_ENDPOINT = "pref_key_api_endpoint";
  private final SharedPreferences settings;

  public SettingsManager(Context context) {
    settings = PreferenceManager.getDefaultSharedPreferences(context);
  }

  public String getApiEndpoint() {
    return settings.getString(KEY_API_ENDPOINT, null);
  }
}
