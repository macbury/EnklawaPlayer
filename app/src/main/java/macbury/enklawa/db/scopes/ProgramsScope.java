package macbury.enklawa.db.scopes;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

import macbury.enklawa.api.APIProgram;
import macbury.enklawa.db.models.EpisodeFile;
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

  public List<Program> allOrderedByName() {
    QueryBuilder<Program, Integer> builder = dao.queryBuilder();
    try {
      return builder.orderBy("name", true).query();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public List<Program> allFavorited() {
    QueryBuilder<Program, Integer> builder = dao.queryBuilder();
    try {
      return builder.orderBy("name", true).where().eq("favorite", true).query();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public boolean updateFromApi(APIProgram apiProgram) {
    Program oldProgram = find(apiProgram);
    Program newProgram = buildFromApi(apiProgram);
    if (oldProgram != null) {
      newProgram.favorite = oldProgram.favorite;
    }
    return update(newProgram);
  }

  @Override
  public void afterCreate(Program model) {

  }

  @Override
  public void afterDestroy(Program object) {

  }

  @Override
  public void afterSave(Program object) {

  }
}
