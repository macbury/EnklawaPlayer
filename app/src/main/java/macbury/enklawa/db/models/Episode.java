package macbury.enklawa.db.models;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import org.joda.time.DateTime;

import java.sql.SQLException;
import java.util.Date;

/**
 * Created by macbury on 10.09.14.
 */

@DatabaseTable(tableName = "episodes")
public class Episode extends BaseModel {
  private static final int FRESH_AFTER_DAYS = 3;
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
  @ForeignCollectionField
  private ForeignCollection<EpisodeFile> files;

  @DatabaseField
  public float playedDuration = 0f;
  @DatabaseField
  public boolean played = false;
  @DatabaseField
  public String description;
  private EpisodeFile file;

  public boolean isFresh() {
    DateTime fromDate = new DateTime();
    fromDate = fromDate.minusDays(FRESH_AFTER_DAYS);
    return pubDate.after(fromDate.toDate());
  }

  public boolean isProgramFavorite() {
    return this.program.favorite;
  }

  private boolean isPlayed() {
    return played;
  }

  public boolean isDownloaded() {
    return getFile() != null && getFile().isDownloadedAndExists();
  }

  public EpisodeFile getFile() {
    try {
      files.refreshCollection();
      return files.size() > 0 ? files.iteratorThrow().first() : null;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public boolean isUnread() {
    return !played;
  }

  public int getDuration() {
    return duration;
  }

}
