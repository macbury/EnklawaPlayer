package macbury.enklawa.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by macbury on 09.09.14.
 */

@DatabaseTable(tableName = "programs")
public class Program {
  @DatabaseField(canBeNull = false, id = true)
  public int id;
  @DatabaseField
  public String name;
  @DatabaseField
  public String   description;
  @DatabaseField
  public String   author;
  @DatabaseField
  public boolean  live;
  @DatabaseField
  public String   image;
}
