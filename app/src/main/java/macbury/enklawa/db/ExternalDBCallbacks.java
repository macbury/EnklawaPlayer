package macbury.enklawa.db;

import macbury.enklawa.db.models.BaseModel;

/**
 * Created by macbury on 10.09.14.
 */
public interface ExternalDBCallbacks {
  public void afterCreate(BaseModel model);
  public void afterDestroy(BaseModel object);
  public void afterSave(BaseModel object);
}
