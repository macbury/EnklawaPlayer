package macbury.enklawa.db;

/**
 * Created by macbury on 10.09.14.
 */
public interface DatabaseCRUDListener<T> {
  public void afterCreate(T model);
  public void afterDestroy(T object);
  public void afterSave(T object);
}
