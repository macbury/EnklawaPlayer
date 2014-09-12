package macbury.enklawa.db.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * Created by macbury on 12.09.14.
 */
@DatabaseTable(tableName = "forum_threads")
public class ForumThread extends BaseModel {
  @DatabaseField(canBeNull = false, generatedId = true)
  public int      id;
  @DatabaseField
  public String   title;
  @DatabaseField
  public String   content;
  @DatabaseField(columnName = "pub_date")
  public Date     pubDate;
  @DatabaseField
  public String   link;
}
