package macbury.enklawa.api;

import java.util.ArrayList;

import macbury.enklawa.db.models.Program;

/**
 * Created by macbury on 09.09.14.
 */
public class APIResponse {
  public String version;
  public String skype;
  public String phone;
  public String radio;

  public ArrayList<APICategory> categories;
  public ArrayList<APIProgram> programs;

  public int countProgramsAndEpisodes() {
    int total = 1;
    for(APIProgram program : programs) {
      total++;
      total += program.episodes.size();
    }
    return total;
  }
}
