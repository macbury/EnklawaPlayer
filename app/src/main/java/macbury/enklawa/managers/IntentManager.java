package macbury.enklawa.managers;

import android.content.Intent;

import macbury.enklawa.activities.SettingsActivity;

/**
 * Created by macbury on 09.09.14.
 */
public class IntentManager {
  private final ApplicationManager context;

  public IntentManager(ApplicationManager applicationManager) {
    this.context = applicationManager;
  }

  public Intent showSettingsActivity() {
    Intent intent = new Intent(context, SettingsActivity.class);
    return intent;
  }
}
