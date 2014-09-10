package macbury.enklawa.db.scopes;

import android.util.Log;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import macbury.enklawa.db.DBCallbacks;
import macbury.enklawa.managers.DatabaseManager;

/**
 * Created by macbury on 09.09.14.
 */

// K is for dbObject Class
// T is for apiObject

public abstract class AbstractScope<T, K> {
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

  public abstract K buildFromApi(T apiObject);

  public boolean save(T apiObject) {
    return update(buildFromApi(apiObject));
  }

  public boolean update(K dbObject) {
    try {
      Dao.CreateOrUpdateStatus status = dao.createOrUpdate(dbObject);
      if (DBCallbacks.class.isInstance(dbObject)) {
        if (status.isCreated()) {
          Log.v(getClass().getSimpleName(), "Created...");
          triggerAfterCreateCallback(dbObject);
        } else {
          Log.v(getClass().getSimpleName(), "Updated...");
        }
        triggerAfterSaveCallback(dbObject);
      }

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

  public abstract K find(T apiObject);

  private boolean haveCallbacks(K dbObject) {
    return DBCallbacks.class.isInstance(dbObject);
  }

  private DBCallbacks callbacks(K dbObject) {
    return (DBCallbacks) dbObject;
  }

  private void triggerAfterCreateCallback(K dbObject) {
    if (haveCallbacks(dbObject)) {
      callbacks(dbObject).afterCreate();
    }
  }

  private void triggerAfterSaveCallback(K dbObject) {
    if (haveCallbacks(dbObject)) {
      callbacks(dbObject).afterSave();
    }
  }

  private void triggerAfterDestroyCallback(K dbObject) {
    if (haveCallbacks(dbObject)) {
      callbacks(dbObject).afterDestroy();
    }
  }
}
