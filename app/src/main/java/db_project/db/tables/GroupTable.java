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
import db_project.model.Group;
import db_project.utils.AbstractJsonReader;

public class GroupTable extends AbstractTable<Group, String> implements JsonReadeable<Group> {
  public static final String TABLE_NAME = "COMITIVA";
  public static final String PRIMARY_KEY = "codComitiva";
  private final Logger logger;

  public GroupTable(final Connection connection) {
    super(TABLE_NAME, connection);
    super.setPrimaryKey(PRIMARY_KEY);
    super.setTableColumns(List.of("numPersone"));
    this.logger = Logger.getLogger("CityTable");
    this.logger.setLevel(Level.WARNING);
  }

  @Override
  protected Object[] getSaveQueryParameters(final Group group) {
    return new Object[] {group.getGroupId(), group.getNumberOfPeople()};
  }

  @Override
  protected Object[] getUpdateQueryParameters(final Group group) {
    return new Object[] {group.getNumberOfPeople(), group.getGroupId()};
  }

  @Override
  public boolean createTable() {
    final String query =
        "create table COMITIVA ( "
            + "codComitiva varchar(5) not null, "
            + "numPersone int not null, "
            + "constraint ID_COMITIVA_ID primary key (codComitiva));";
    super.created = super.parser.computeSqlQuery(query, null);
    return super.isCreated();
  }

  @Override
  protected List<Group> getPrettyResultFromQueryResult(QueryResult result) {
    if (result.getResult().isEmpty()) {
      return Collections.emptyList();
    }
    List<Group> groups = new ArrayList<>();
    result
        .getResult()
        .get()
        .forEach(
            row -> {
              logger.info(row.toString());
              final String groupId = (String) row.get("codComitiva");
              final int numberOfPeople = (int) row.get("numPersone");
              groups.add(new Group(groupId, numberOfPeople));
            });
    return groups;
  }

  @Override
  public List<Group> readFromFile() {
    return new AbstractJsonReader<Group>() {}.setFileName("DbGroups.json")
        .retreiveData(Group.class);
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
