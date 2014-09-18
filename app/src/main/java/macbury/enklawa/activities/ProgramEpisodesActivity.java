package macbury.enklawa.activities;

import android.os.Bundle;
import android.view.Menu;

import com.negusoft.holoaccent.activity.AccentActivity;

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
