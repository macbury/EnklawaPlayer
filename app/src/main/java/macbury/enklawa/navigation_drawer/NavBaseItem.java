package macbury.enklawa.navigation_drawer;

import android.content.Context;

/**
 * Created by macbury on 11.09.14.
 */
public abstract class NavBaseItem {
  protected final Context context;

  public NavBaseItem(Context context) {
    this.context = context;
  }

  public abstract int getType();
}
