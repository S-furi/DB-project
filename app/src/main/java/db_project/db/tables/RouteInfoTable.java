package db_project.db.tables;

import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import db_project.db.AbstractCompositeKeyTable;
import db_project.db.queryUtils.QueryResult;
import db_project.model.RouteInfo;

public class RouteInfoTable extends AbstractCompositeKeyTable<RouteInfo, Object> {
  public static final String TABLE_NAME = "PERCORRENZA";
  public static final List<String> PRIMARY_KEYS = List.of("codPercorso", "codTreno", "data");

  public RouteInfoTable(final Connection connection) {
    super(TABLE_NAME, connection);
    super.setPrimaryKey(PRIMARY_KEYS);
    super.setTableColumns(Collections.emptyList());
  }

  @Override
  protected Object[] getSaveQueryParameters(final RouteInfo routeInfo) {
    return new Object[] {routeInfo.getPathId(), routeInfo.getTrainId(), routeInfo.getDate()};
  }

  @Override
  protected Object[] getUpdateQueryParameters(final RouteInfo routeInfo) {
    return new Object[] {
      routeInfo.getPathId(),
      routeInfo.getTrainId(),
      routeInfo.getDate(),
      routeInfo.getPathId(),
      routeInfo.getTrainId(),
      routeInfo.getDate()
    };
  }

  @Override
  protected List<RouteInfo> getPrettyResultFromQueryResult(final QueryResult result) {
    if (result.getResult().isEmpty()) {
      return Collections.emptyList();
    }
    List<RouteInfo> routeInfos = new ArrayList<>();
    result
        .getResult()
        .get()
        .forEach(
            row -> {
              System.out.println(row.toString());
              final String pathId = (String) row.get("codPercorso");
              final String trainId = (String) row.get("codTreno");
              final Date date = (Date) row.get("data");
              routeInfos.add(new RouteInfo(pathId, trainId, date));
            });
    return routeInfos;
  }
  
  @Override
  public boolean createTable() {
    final String query = 
      "create table PERCORRENZA ( " +
      "codPercorso varchar(5) not null, " +
      "codTreno varchar(5) not null, " +
      "data date not null, " +
      "constraint ID_PERCORRENZA_ID primary key (codPercorso, codTreno, data)); ";
    super.created = super.parser.computeSqlQuery(query, null);
    return super.isCreated();
  }

  /** In this paricular table, updates aren't possible... :( */
  @Override
  public boolean update(RouteInfo updatedValue) {
    return false;
  }
}
