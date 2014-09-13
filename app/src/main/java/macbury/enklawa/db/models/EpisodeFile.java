package macbury.enklawa.db.models;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.File;
import java.io.OutputStream;

import macbury.enklawa.db.DBCallbacks;

/**
 * Created by macbury on 11.09.14.
 */
@DatabaseTable(tableName = "episode_files")
public class EpisodeFile extends BaseModel implements DBCallbacks {
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

  @Override
  public void afterCreate() {

  }

  @Override
  public void afterDestroy() {
    Log.wtf("EpisodeFile", "Remove file here!");
  }

  @Override
  public void afterSave() {

  }

  public boolean isDownloadedAndExists() {
    return status == Status.Ready;
  }

  public File file(Context context) {
    return new File(context.getFilesDir(), getFileName());
  }

  private String getFileName() {
    return "episode_"+id+".mp3";
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

}
