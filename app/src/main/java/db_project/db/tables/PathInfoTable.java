package db_project.db.tables;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import db_project.db.AbstractTable;
import db_project.db.queryUtils.QueryResult;
import db_project.model.PathInfo;

public class PathInfoTable extends AbstractTable<PathInfo, String> {
  public static final String TABLE_NAME = "DETTAGLIO_PERCORSO";
  public static final String PRIMARY_KEY = "codTratta";

  public PathInfoTable(final Connection connection) {
    super(TABLE_NAME, connection);
    super.setPrimaryKey(PRIMARY_KEY);
    super.setTableColumns(List.of("ordine", "codPercorso"));
  }

  @Override
  protected Object[] getSaveQueryParameters(final PathInfo pathInfo) {
    return new Object[] {pathInfo.getSectionId(), pathInfo.getOrderNumber(), pathInfo.getPathId()};
  }

  @Override
  protected Object[] getUpdateQueryParameters(final PathInfo pathInfo) {
    return new Object[] {pathInfo.getOrderNumber(), pathInfo.getPathId(), pathInfo.getSectionId()};
  }

  @Override
  public boolean createTable() {
    final String query =
        "create table DETTAGLIO_PERCORSO ( "
            + "codTratta varchar(5) not null, "
            + "ordine int not null, "
            + "codPercorso varchar(5) not null, "
            + "constraint FKStr_TRA_ID primary key (codTratta));";
    super.created = super.parser.computeSqlQuery(query, null);
    return super.isCreated();
  }

  @Override
  protected List<PathInfo> getPrettyResultFromQueryResult(final QueryResult result) {
    if (result.getResult().isEmpty()) {
      return Collections.emptyList();
    }
    List<PathInfo> pathInfos = new ArrayList<>();
    result
        .getResult()
        .get()
        .forEach(
            row -> {
              System.out.println(row.toString());
              final String sectionId = (String) row.get("codTratta");
              final int orderNumber = (int) row.get("ordine");
              final String pathId = (String) row.get("codPercorso");
              pathInfos.add(new PathInfo(sectionId, orderNumber, pathId));
            });
    return pathInfos;
  }
}
