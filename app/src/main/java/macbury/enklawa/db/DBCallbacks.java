package macbury.enklawa.db;

/**
 * Created by macbury on 10.09.14.
 */
public interface DBCallbacks {

  public void afterCreate();
  public void afterDestroy();
  public void afterSave();
}
