package macbury.enklawa.db.models;

import macbury.enklawa.db.DBCallbacks;

/**
 * Created by macbury on 10.09.14.
 */
public class BaseModel {
  private DBCallbacks listener;

  public void setListener(DBCallbacks listener) {
    this.listener = listener;
  }
}
