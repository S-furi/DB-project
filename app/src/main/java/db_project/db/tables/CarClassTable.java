package db_project.db.tables;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import db_project.db.AbstractTable;
import db_project.db.queryUtils.QueryResult;
import db_project.model.CarClass;

public class CarClassTable extends AbstractTable<CarClass, Integer> {

  public static String TABLE_NAME = "CLASSE";
  public static String primary_key = "numClasse";

  public CarClassTable(final Connection connection) {
    super(TABLE_NAME, connection);
    super.setPrimaryKey(primary_key);
    super.setTableColumns(List.of("postiDisponibili"));
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
    return false;
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
              System.out.println(row.toString());
              final int classNum = (int) row.get("numClasse");
              final int availableSeats = (int) row.get("postiDisponibili");
              carClasses.add(new CarClass(classNum, availableSeats));
            });
    return carClasses;
  }
}
