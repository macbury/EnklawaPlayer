package macbury.enklawa.fragments.main.episodes;

import java.util.List;

import macbury.enklawa.db.models.Episode;
import macbury.enklawa.db.models.Program;
import macbury.enklawa.fragments.main.AbstractEpisodesFragment;

/**
 * Created by macbury on 15.09.14.
 */
public class ProgramEpisodesFragment extends AbstractEpisodesFragment {
  private final Program program;

  public ProgramEpisodesFragment(Program program) {
    super();
    this.program = program;
  }

  @Override
  public List<Episode> getEpisodes() {
    return app.db.episodes.allForProgram(program);
  }
}
