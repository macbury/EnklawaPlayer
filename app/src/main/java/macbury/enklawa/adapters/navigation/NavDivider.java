package macbury.enklawa.adapters.navigation;

import android.content.Context;

/**
 * Created by macbury on 11.09.14.
 */
public class NavDivider extends NavBaseItem {
  private String title;

  public NavDivider(Context context, String title) {
    super(context);
    setTitle(title);
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Override
  public int getType() {
    return 0;
  }
}
