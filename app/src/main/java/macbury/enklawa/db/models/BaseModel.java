package macbury.enklawa.db.models;

import macbury.enklawa.db.DBCallbacks;
import macbury.enklawa.db.ExternalDBCallbacks;

/**
 * Created by macbury on 10.09.14.
 */
public class BaseModel {
  private ExternalDBCallbacks listener;

  public void setListener(ExternalDBCallbacks listener) {
    this.listener = listener;
  }

  public ExternalDBCallbacks getListener() {
    return listener;
  }
}
