package macbury.enklawa.db.scopes;

import android.util.Log;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import macbury.enklawa.db.DatabaseCRUDListener;

/**
 * Created by macbury on 09.09.14.
 */

// K is for dbObject Class
// T is for apiObject

public abstract class AbstractScope<K> implements DatabaseCRUDListener<K> {
  protected Dao<K, Integer> dao;
  protected ArrayList<DatabaseCRUDListener<K>> listeners;

  public AbstractScope(Dao<K, Integer> dao) {
    this.dao       = dao;
    this.listeners = new ArrayList<DatabaseCRUDListener<K>>();
    addListener(this);
  }

  public void addListener(DatabaseCRUDListener<K> listener) {
    if (listeners.indexOf(listener) == -1) {
      listeners.add(listener);
    }
  }

  public void removeListener(DatabaseCRUDListener<K> listener) {
    if (listeners.indexOf(listener) != -1) {
      listeners.remove(listener);
    }
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

  private void triggerAfterCreateCallback(K dbObject) {
    for (DatabaseCRUDListener listener : listeners) {
      listener.afterCreate(dbObject);
    }
  }

  private void triggerAfterSaveCallback(K dbObject) {
    for (DatabaseCRUDListener listener : listeners) {
      listener.afterSave(dbObject);
    }
  }

  private void triggerAfterDestroyCallback(K dbObject) {
    for (DatabaseCRUDListener listener : listeners) {
      listener.afterDestroy(dbObject);
    }
  }


}
