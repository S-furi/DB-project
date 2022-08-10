package db_project.db.tables;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import db_project.db.AbstractTable;
import db_project.db.queryUtils.QueryResult;
import db_project.model.Group;

public class GroupTable extends AbstractTable<Group, String> {
  public static final String TABLE_NAME = "COMITIVA";
  public static final String PRIMARY_KEY = "codComitiva";

  public GroupTable(final Connection connection) {
    super(TABLE_NAME, connection);
    super.setPrimaryKey(PRIMARY_KEY);
    super.setTableColumns(List.of("numPersone"));
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
      return false;
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
              System.out.println(row.toString());
              final String groupId = (String) row.get("codComitiva");
              final int numberOfPeople = (int) row.get("numPersone");
              groups.add(new Group(groupId, numberOfPeople));
            });
    return groups;
  }
}
