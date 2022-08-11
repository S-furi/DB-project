package db_project.db.tables;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import db_project.db.AbstractTable;
import db_project.db.queryUtils.QueryResult;
import db_project.model.Path;
;

public class PathTable extends AbstractTable<Path, String> {
  public static final String TABLE_NAME = "PERCORSO";
  public static final String PRIMARY_KEY = "codPercorso";
  private final Logger logger;

  public PathTable(final Connection connection) {
    super(TABLE_NAME, connection);
    super.setPrimaryKey(PRIMARY_KEY);
    super.setTableColumns(List.of("tempoTotale", "numFermate", "adminID"));
    this.logger = Logger.getLogger("CityTable");
    this.logger.setLevel(Level.WARNING);
  }

  @Override
  protected Object[] getSaveQueryParameters(final Path path) {
    return new Object[] {
      path.getPathCode(), path.getTotalTime(), path.getStops(), path.getAdminID()
    };
  }

  @Override
  protected Object[] getUpdateQueryParameters(final Path path) {
    return new Object[] {
      path.getTotalTime(), path.getStops(), path.getAdminID(), path.getPathCode()
    };
  }

  @Override
  public boolean createTable() {
    final String query =
        "create table PERCORSO ( "
            + "codPercorso varchar(5) not null, "
            + "tempoTotale varchar(5) not null, "
            + "numFermate int not null, "
            + "adminID varchar(5) not null, "
            + "constraint ID_PERCORSO_ID primary key (codPercorso));";
    super.created = super.parser.computeSqlQuery(query, null);
    return super.isCreated();
  }

  @Override
  protected List<Path> getPrettyResultFromQueryResult(final QueryResult result) {
    if (result.getResult().isEmpty()) {
      return Collections.emptyList();
    }
    final List<Path> pathList = new ArrayList<>();

    result
        .getResult()
        .get()
        .forEach(
            row -> {
              logger.info(row.toString());
              final String pathCode = (String) row.get("codPercorso");
              final String totalTime = (String) row.get("tempoTotale");
              final int stops = (int) row.get("numFermate");
              final String adminID = (String) row.get("adminID");
              pathList.add(new Path(pathCode, totalTime, stops, adminID));
            });

    return pathList;
  }
}
