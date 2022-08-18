package db_project.db.tables;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import db_project.db.AbstractTable;
import db_project.db.JsonReadeable;
import db_project.db.queryUtils.QueryResult;
import db_project.model.CarClass;
import db_project.utils.AbstractJsonReader;

public class CarClassTable extends AbstractTable<CarClass, String>
    implements JsonReadeable<CarClass> {
  public static String TABLE_NAME = "CLASSE";
  public static String primary_key = "numClasse";
  private final Logger logger;

  public CarClassTable(final Connection connection) {
    super(TABLE_NAME, connection);
    super.setPrimaryKey(primary_key);
    super.setTableColumns(List.of("postiDisponibili"));
    this.logger = Logger.getLogger("CarClassTable");
    this.logger.setLevel(Level.WARNING);
  }

  @Override
  protected Object[] getSaveQueryParameters(CarClass carClass) {
    return new Object[] {carClass.getClassType(), carClass.getAvailableSeats()};
  }

  @Override
  protected Object[] getUpdateQueryParameters(CarClass carclass) {
    return new Object[] {carclass.getAvailableSeats(), carclass.getClassType()};
  }

  @Override
  public boolean createTable() {
    final String query =
        "create table CLASSE ( "
            + "numClasse varchar(1) not null, "
            + "postiDisponibili int not null, "
            + "constraint ID_CLASSE_ID primary key (numClasse));";
    super.created = super.parser.computeSqlQuery(query, null);
    return super.created;
  }

  @Override
  protected List<CarClass> getPrettyResultFromQueryResult(QueryResult result) {
    if (result.getResult().isEmpty()) {
      return Collections.emptyList();
    }
    List<CarClass> carClasses = new ArrayList<>();
    result
        .getResult()
        .get()
        .forEach(
            row -> {
              logger.info(row.toString());
              final String classNum = (String) row.get("numClasse");
              final int availableSeats = (int) row.get("postiDisponibili");
              carClasses.add(new CarClass(classNum, availableSeats));
            });
    return carClasses;
  }

  @Override
  public List<CarClass> readFromFile() {
    return new AbstractJsonReader<CarClass>() {}.setFileName("DbCarClass.json")
        .retreiveData(CarClass.class);
  }

  @Override
  public boolean saveToDb() {
    for (final var elem : this.readFromFile()) {
      try {
        if (!this.save(elem)) {
          return false;
        }
      } catch (final IllegalStateException e) {
        return false;
      }
    }
    return true;
  }
}
