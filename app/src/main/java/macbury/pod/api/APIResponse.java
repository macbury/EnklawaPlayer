package macbury.pod.api;

import java.util.ArrayList;

/**
 * Created by macbury on 09.09.14.
 */
public class APIResponse {
  public String version;
  public String skype;
  public String phone;
  public String radio;

  public ArrayList<APICategory> categories;
  public ArrayList<APIProgram>  programs;
  public ArrayList<APIThread>   forum;

  public int countProgramsAndEpisodes() {
    int total = 1 + forum.size();
    for(APIProgram program : programs) {
      total++;
      total += program.episodes.size();
    }
    return total;
  }
}
