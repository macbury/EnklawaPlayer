package macbury.enklawa.db.scopes;

import com.j256.ormlite.dao.Dao;

import macbury.enklawa.api.APIProgram;
import macbury.enklawa.db.models.Program;

/**
 * Created by macbury on 09.09.14.
 */
public class ProgramsScope extends AbstractScope<Program> {

  public ProgramsScope(Dao<Program, Integer> dao) {
    super(dao);
  }

  public Program buildFromApi(APIProgram apiObject) {
    Program program     = new Program();
    program.id          = apiObject.id;
    program.name        = apiObject.name;
    program.description = apiObject.description;
    program.author      = apiObject.author;
    program.image       = apiObject.image;
    program.live        = apiObject.live;
    return program;
  }

  public Program find(APIProgram apiObject) {
    return find(apiObject.id);
  }
}
