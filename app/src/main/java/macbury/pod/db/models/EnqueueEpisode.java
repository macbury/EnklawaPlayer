package macbury.pod.db.models;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import macbury.pod.managers.player.PlaybackStatus;

/**
 * Created by macbury on 23.09.14.
 */
@DatabaseTable(tableName = "enqueue_episodes")
public class EnqueueEpisode {

  @DatabaseField(canBeNull = false, generatedId = true)
  public int      id;
  @DatabaseField(canBeNull = false, defaultValue = "0")
  public int      position;
  @DatabaseField(canBeNull = false, defaultValue = "0")
  public int      time;
  @DatabaseField(dataType = DataType.ENUM_STRING, defaultValue = "Pending")
  public PlaybackStatus status;
  @DatabaseField(foreign=true, foreignAutoRefresh=true)
  public Episode  episode;
}
