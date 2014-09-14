package macbury.enklawa.db.scopes;

import android.util.Log;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import macbury.enklawa.db.DBCallbacks;
import macbury.enklawa.db.models.BaseModel;

/**
 * Created by macbury on 09.09.14.
 */

// K is for dbObject Class
// T is for apiObject

public abstract class AbstractScope<K> {
  protected Dao<K, Integer> dao;

  public AbstractScope(Dao<K, Integer> dao) {
    this.dao = dao;
  }

  public List<K> all() {
    try {
      return dao.queryForAll();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public boolean update(K dbObject) {
    try {
      Dao.CreateOrUpdateStatus status = dao.createOrUpdate(dbObject);
      if (status.isCreated()) {
        Log.v(getClass().getSimpleName(), "Created...");
        triggerAfterCreateCallback(dbObject);
      } else {
        Log.v(getClass().getSimpleName(), "Updated...");
      }
      triggerAfterSaveCallback(dbObject);

      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }


  public K find(int id) {
    try {
      return dao.queryForId(id);
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public boolean destroy(K dbObject) {
    try {
      dao.delete(dbObject);
      triggerAfterDestroyCallback(dbObject);
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public long count() {
    try {
      return dao.countOf();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return 0;
  }

  private boolean haveCallbacksInterface(K dbObject) {
    return DBCallbacks.class.isInstance(dbObject);
  }

  private boolean haveCallbacks(K dbObject) {
    return dbObject != null && callbacks(dbObject).getListener() != null;
  }

  private DBCallbacks callbacksInterface(K dbObject) {
    return (DBCallbacks) dbObject;
  }

  private BaseModel callbacks(K dbObject) {
    return (BaseModel) dbObject;
  }

  private void triggerAfterCreateCallback(K dbObject) {
    if (haveCallbacksInterface(dbObject)) {
      callbacksInterface(dbObject).afterCreate();
    }

    if (haveCallbacks(dbObject)) {
      callbacks(dbObject).getListener().afterCreate((BaseModel)dbObject);
    }
  }

  private void triggerAfterSaveCallback(K dbObject) {
    if (haveCallbacksInterface(dbObject)) {
      callbacksInterface(dbObject).afterSave();
    }

    if (haveCallbacks(dbObject)) {
      callbacks(dbObject).getListener().afterSave((BaseModel)dbObject);
    }
  }

  private void triggerAfterDestroyCallback(K dbObject) {
    if (haveCallbacksInterface(dbObject)) {
      callbacksInterface(dbObject).afterDestroy();
    }

    if (haveCallbacks(dbObject)) {
      callbacks(dbObject).getListener().afterDestroy((BaseModel)dbObject);
    }
  }
}
