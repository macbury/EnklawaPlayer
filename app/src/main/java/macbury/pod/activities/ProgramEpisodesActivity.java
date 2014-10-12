package macbury.pod.activities;

import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.negusoft.holoaccent.activity.AccentActivity;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import macbury.pod.R;
import macbury.pod.db.models.Program;
import macbury.pod.fragments.main.episodes.ProgramEpisodesFragment;
import macbury.pod.managers.App;

/**
 * Created by macbury on 15.09.14.
 */
public class ProgramEpisodesActivity extends AccentActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    App app           = App.current();
    Program program       = app.db.programs.find(app.intents.getProgramId(getIntent()));

    getActionBar().setTitle(program.name);
    getActionBar().setDisplayUseLogoEnabled(false);
    getActionBar().setDisplayShowTitleEnabled(true);
    ProgramEpisodesFragment fragment = new ProgramEpisodesFragment();
    fragment.setProgram(program);

    setContentView(R.layout.activity_program_episodes);
    getFragmentManager().beginTransaction()
            .replace(R.id.content_frame, fragment)
            .commit();

    SystemBarTintManager tintManager = new SystemBarTintManager(this);
    tintManager.setStatusBarTintEnabled(true);
    tintManager.setStatusBarTintResource(R.color.statusbar_color);

    View mainContentView = findViewById(R.id.content_frame);
    SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      mainContentView.setPadding(0, config.getPixelInsetTop(true), config.getPixelInsetRight(), config.getPixelInsetBottom());
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    return super.onCreateOptionsMenu(menu);
  }
}
