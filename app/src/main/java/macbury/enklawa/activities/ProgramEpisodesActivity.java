package macbury.enklawa.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

import com.koushikdutta.ion.Ion;
import com.negusoft.holoaccent.activity.AccentActivity;

import macbury.enklawa.R;
import macbury.enklawa.db.models.Program;
import macbury.enklawa.fragments.SettingsFragment;
import macbury.enklawa.fragments.main.episodes.NewestEpisodesFragment;
import macbury.enklawa.fragments.main.episodes.ProgramEpisodesFragment;
import macbury.enklawa.managers.ApplicationManager;

/**
 * Created by macbury on 15.09.14.
 */
public class ProgramEpisodesActivity extends AccentActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ApplicationManager app           = ApplicationManager.current();
    Program program                  = app.db.programs.find(app.intents.getProgramId(getIntent()));

    getActionBar().setTitle(program.name);

    ProgramEpisodesFragment fragment = new ProgramEpisodesFragment();
    fragment.setProgram(program);

    setContentView(R.layout.activity_program_episodes);
    getFragmentManager().beginTransaction()
            .replace(R.id.content_frame, fragment)
            .commit();

    //ImageView programPreview = (ImageView)findViewById(R.id.program_preview);
    //Ion.with(this).load(program.image).intoImageView(programPreview);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    return super.onCreateOptionsMenu(menu);
  }
}
