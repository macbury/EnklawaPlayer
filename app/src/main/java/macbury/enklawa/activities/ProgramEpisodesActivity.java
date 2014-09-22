package macbury.enklawa.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.negusoft.holoaccent.activity.AccentActivity;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import macbury.enklawa.R;
import macbury.enklawa.db.models.Program;
import macbury.enklawa.fragments.main.episodes.ProgramEpisodesFragment;
import macbury.enklawa.managers.Enklawa;

/**
 * Created by macbury on 15.09.14.
 */
public class ProgramEpisodesActivity extends AccentActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Enklawa app           = Enklawa.current();
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
    mainContentView.setPadding(0, config.getPixelInsetTop(true), config.getPixelInsetRight(), config.getPixelInsetBottom());
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    return super.onCreateOptionsMenu(menu);
  }
}
