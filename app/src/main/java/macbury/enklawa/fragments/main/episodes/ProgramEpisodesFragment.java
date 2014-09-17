package macbury.enklawa.fragments.main.episodes;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import macbury.enklawa.R;
import macbury.enklawa.db.models.Episode;
import macbury.enklawa.db.models.Program;
import macbury.enklawa.dialogs.ProgramAboutDialog;
import macbury.enklawa.fragments.main.AbstractEpisodesFragment;
import macbury.enklawa.managers.Enklawa;

/**
 * Created by macbury on 15.09.14.
 */
public class ProgramEpisodesFragment extends AbstractEpisodesFragment {
  private Program program;

  public ProgramEpisodesFragment() {
    super();
  }

  @Override
  public List<Episode> getEpisodes() {
    return app.db.episodes.allForProgram(program);
  }

  public void setProgram(Program program) {
    this.program = program;
    if (isAdded()) {
      updateEpisodes();
      getActivity().invalidateOptionsMenu();
    }
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.program, menu);

    MenuItem item = menu.findItem(R.id.action_favorite);

    if (program.isFavorite()) {
      item.setIcon(R.drawable.ic_action_rating_important);
      item.setTitle(R.string.action_mark_as_not_favorite);
    } else {
      item.setIcon(R.drawable.ic_action_rating_not_important);
      item.setTitle(R.string.action_mark_as_favorite);
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    switch (item.getItemId()) {
      case R.id.action_favorite:
        toggleFavorite();
      break;

      case R.id.action_about:
        ProgramAboutDialog dialog = new ProgramAboutDialog(getActivity(), program);
        dialog.show();
      break;
    }

    return super.onOptionsItemSelected(item);
  }

  private void toggleFavorite() {
    Enklawa app = Enklawa.current();
    program.favorite       = !program.favorite;
    app.db.programs.update(program);
    app.broadcasts.favoriteProgramChange(program);
    Toast.makeText(getActivity(), program.favorite ? R.string.action_marked_as_favorite : R.string.action_marked_as_not_favorite, Toast.LENGTH_SHORT).show();
    getActivity().invalidateOptionsMenu();
  }

  public Program getProgram() {
    return program;
  }
}
