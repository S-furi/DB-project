package db_project.db.tables;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import db_project.db.AbstractCompositeKeyTable;
import db_project.db.JsonReadeable;
import db_project.db.queryUtils.QueryResult;
import db_project.model.PathInfo;
import db_project.utils.AbstractJsonReader;

public class PathInfoTable extends AbstractCompositeKeyTable<PathInfo, Object>
    implements JsonReadeable<PathInfo> {
  public static final String TABLE_NAME = "DETTAGLIO_PERCORSO";
  public static final List<String> PRIMARY_KEY = List.of("codPercorso", "ordine", "codTratta");
  private final Logger logger;

  public PathInfoTable(final Connection connection) {
    super(TABLE_NAME, connection);
    super.setPrimaryKey(PRIMARY_KEY);
    super.setTableColumns(Collections.emptyList());
    this.logger = Logger.getLogger("CityTable");
    this.logger.setLevel(Level.WARNING);
  }

  @Override
  protected Object[] getSaveQueryParameters(final PathInfo pathInfo) {
    return new Object[] {pathInfo.getPathId(), pathInfo.getOrderNumber(), pathInfo.getSectionId()};
  }

  @Override
  protected Object[] getUpdateQueryParameters(final PathInfo pathInfo) {
    return new Object[] {
      pathInfo.getPathId(),
      pathInfo.getOrderNumber(),
      pathInfo.getSectionId(),
      pathInfo.getPathId(),
      pathInfo.getOrderNumber(),
      pathInfo.getSectionId()
    };
  }

  @Override
  public boolean createTable() {
    final String query =
        "create table DETTAGLIO_PERCORSO ( "
            + "codTratta varchar(5) not null, "
            + "codPercorso varchar(5) not null, "
            + "ordine varchar(10) not null, "
            + "constraint ID_DETTAGLIO_PERCORSO_ID primary key (codPercorso, codTratta, ordine)); ";

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
              logger.info(row.toString());
              final String sectionId = (String) row.get("codTratta");
              final String orderNumber = (String) row.get("ordine");
              final String pathId = (String) row.get("codPercorso");
              pathInfos.add(new PathInfo(sectionId, orderNumber, pathId));
            });
    return pathInfos;
  }

  @Override
  public boolean update(final PathInfo updatedValue) {
    return false;
  }

  @Override
  public List<PathInfo> readFromFile() {
    return new AbstractJsonReader<PathInfo>() {}.setFileName("DbPathInfo.json")
        .retreiveData(PathInfo.class);
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
