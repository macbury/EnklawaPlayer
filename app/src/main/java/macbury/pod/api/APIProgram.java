package macbury.pod.api;

import java.util.ArrayList;

/**
 * Created by macbury on 09.09.14.
 */
public class APIProgram {
  public int      id;
  public String   name;
  public String   description;
  public String   author;
  public boolean  live;
  public String   image;
  public String   feed_url;
  public int      category_id;

  public ArrayList<APIEpisode> episodes;
}
