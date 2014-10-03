package macbury.enklawa.activities;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.negusoft.holoaccent.activity.AccentActivity;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import macbury.enklawa.R;
import macbury.enklawa.managers.Enklawa;

public class DonateActivity extends AccentActivity implements View.OnClickListener {
  private final static String DONTAE_PAGE_URL                 = "http://macbury.ninja/donate/bitcoin";
  private static final String GITHUB_PAGE_URL                 = "https://github.com/macbury/EnklawaPlayer";
  private Button donationButton;
  private Button githubButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_donate);
    SystemBarTintManager tintManager = new SystemBarTintManager(this);
    tintManager.setStatusBarTintEnabled(true);
    tintManager.setStatusBarTintResource(R.color.statusbar_color);

    View mainContainer = findViewById(R.id.main_container);
    SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      mainContainer.setPadding(0, config.getPixelInsetTop(true), config.getPixelInsetRight(), config.getPixelInsetBottom());
    }
    donationButton     = (Button) findViewById(R.id.button_donate);
    githubButton       = (Button) findViewById(R.id.button_github);

    donationButton.setOnClickListener(this);
    githubButton.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    if (v == donationButton) {
      startActivity(Enklawa.current().intents.openURL(DONTAE_PAGE_URL));
    } else {
      startActivity(Enklawa.current().intents.openURL(GITHUB_PAGE_URL));
    }
  }
}
