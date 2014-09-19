package macbury.enklawa.managers.player;

import java.io.File;
import java.net.URI;

/**
 * Created by macbury on 19.09.14.
 */
public abstract class PlayerMediaSource {
  public abstract boolean isRemote();
  public abstract URI     getURL();
  public abstract File    getFile();
  public abstract URI     getArtWork();
  public abstract String  getTitle();
  public abstract String  getAuthor();
  public abstract String  getDetails();
}
