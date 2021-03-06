package macbury.pod.navigation_drawer.items;

import android.content.Context;

import macbury.pod.db.models.Program;
import macbury.pod.navigation_drawer.NavBaseItem;

/**
 * Created by macbury on 16.09.14.
 */
public class FavoriteProgramNavItem extends NavBaseItem {
  private final Program program;

  public FavoriteProgramNavItem(Context context, Program program) {
    super(context);
    this.program = program;
  }

  @Override
  public int getType() {
    return 2;
  }

  public Program getProgram() {
    return program;
  }
}
