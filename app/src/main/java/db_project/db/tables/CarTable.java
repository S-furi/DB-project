package db_project.db.tables;

import java.sql.Connection;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import db_project.db.AbstractCompositeKeyTable;
import db_project.db.queryUtils.QueryResult;
import db_project.model.Car;

public class CarTable extends AbstractCompositeKeyTable<Car, Object> {
  public static final String TABLE_NAME = "CARROZZA";
  public static final List<String> PRIMARY_KEY = List.of("numClasse", "codTreno", "numeroCarrozza");
  private final Logger logger;

  public CarTable(final Connection connection) {
    super(TABLE_NAME, connection);
    super.setPrimaryKey(PRIMARY_KEY);
    super.setTableColumns(List.of("maxPosti", "bagno"));
    this.logger = Logger.getLogger("CarTable");
    this.logger.setLevel(Level.WARNING);
  }

  @Override
  public boolean createTable() {
    final String query =
        "create table CARROZZA ( "
            + "numClasse varchar(1) not null, "
            + "codTreno varchar(5) not null, "
            + "numeroCarrozza int not null, "
            + "maxPosti int not null, "
            + "bagno char, "
            + "constraint ID_CARROZZA_ID primary key (numClasse, codTreno, numeroCarrozza)); ";
    super.created = super.parser.computeSqlQuery(query, null);
    return super.isCreated();
  }

  @Override
  protected Object[] getSaveQueryParameters(final Car car) {
    return new Object[] {
      car.getClassType(),
      car.getTrainCode(),
      car.getPosition(),
      car.getSeats(),
      car.hasToilet() ? "1" : "0"
    };
  }

  @Override
  protected Object[] getUpdateQueryParameters(final Car car) {
    return new Object[] {
      car.getClassType(),
      car.getTrainCode(),
      car.getPosition(),
      car.getSeats(),
      car.hasToilet() ? "1" : "0",
      car.getClassType(),
      car.getTrainCode(),
      car.getPosition()
    };
  }

  @Override
  protected List<Car> getPrettyResultFromQueryResult(QueryResult result) {
    if (result.getResult().isEmpty()) {
      return Collections.emptyList();
    }
    List<Car> cars = new ArrayList<>();
    result
        .getResult()
        .get()
        .forEach(
            row -> {
              logger.info(row.toString());
              final String classNum = (String) row.get("numClasse");
              final String trainCode = (String) row.get("codTreno");
              final int carNumber = (int) row.get("numeroCarrozza");
              final int maxSeats = (int) row.get("maxPosti");
              final boolean toilet = row.get("bagno").equals("1") ? true : false;

              cars.add(new Car(classNum, trainCode, carNumber, maxSeats, toilet));
            });
    return cars;
  }
}
