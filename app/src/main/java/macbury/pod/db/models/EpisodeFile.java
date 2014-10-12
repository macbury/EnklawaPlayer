package macbury.pod.db.models;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;


/**
 * Created by macbury on 11.09.14.
 */
@DatabaseTable(tableName = "episode_files")
public class EpisodeFile  {
  public static final int MAX_RETRY = 5;

  public enum Status {
    Pending, Downloading, Ready, Failed
  }

  @DatabaseField(canBeNull = false, generatedId = true)
  public int      id;
  @DatabaseField(dataType = DataType.ENUM_STRING, defaultValue = "Pending")
  public Status   status;
  @DatabaseField(columnName = "retry_count", defaultValue = "0")
  public int retryCount = 0;
  @DatabaseField(foreign=true, foreignAutoRefresh=true)
  public Episode  episode;
  @DatabaseField(columnName = "created_at")
  public Date createdAt;


  public boolean isDownloadedAndExists() {
    return status == Status.Ready;
  }


  public boolean haveFailed() {
    return status == Status.Failed;
  }

  public boolean isPending() {
    return status == Status.Pending;
  }

  public boolean isDownloading() {
    return status == Status.Downloading;
  }

  public boolean inDownloadQueue() {
    return isDownloading() || isPending();
  }

  @Override
  public boolean equals(Object o) {
    if (EpisodeFile.class.isInstance(o)) {
      EpisodeFile epf = (EpisodeFile)o;
      return epf.id == id;
    } else {
      return super.equals(o);
    }
  }
}
