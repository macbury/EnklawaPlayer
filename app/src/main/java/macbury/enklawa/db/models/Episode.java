package macbury.enklawa.db.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * Created by macbury on 10.09.14.
 */

@DatabaseTable(tableName = "episodes")
public class Episode extends BaseModel {
  @DatabaseField(canBeNull = false, id = true)
  public int id;
  @DatabaseField
  public String name;
  @DatabaseField
  public String   mp3;
  @DatabaseField(columnName = "pub_date")
  public Date pubDate;
  @DatabaseField
  public String link;
  @DatabaseField
  public int duration;
  @DatabaseField
  public String image;
  @DatabaseField(foreign=true, foreignAutoRefresh=true)
  public Program program;
}
