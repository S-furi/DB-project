package db_project.db.tables;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;


import db_project.db.Table;
import db_project.db.queryUtils.ArrayQueryParser;
import db_project.db.queryUtils.QueryParser;
import db_project.db.queryUtils.QueryResult;
import db_project.model.Group;
import javafx.util.Pair;

public class GroupTable implements Table<Group, String> {
  public static final String TABLE_NAME = "COMITIVA";

  private final Connection connection;
  private final QueryParser queryParser;

  public GroupTable(final Connection connection) {
    this.connection = connection;
    this.queryParser = new ArrayQueryParser(this.connection);
  }

  @Override
  public String getTableName() {
    return TABLE_NAME;
  }

  @Override
  public Optional<Group> findByPrimaryKey(final String primaryKey) {
    final String query = "SELECT * FROM " + TABLE_NAME + " WHERE codComitiva = ?";
    String[] params = {primaryKey};
    this.queryParser.computeSqlQuery(query, params);
    return this.getGroupsFromQueryResult(this.queryParser.getQueryResult()).stream().findAny();
  }

  @Override
  public List<Group> findAll() {
    final String query = "SELECT * FROM " + TABLE_NAME;
    this.queryParser.computeSqlQuery(query, null);
    return this.getGroupsFromQueryResult(this.queryParser.getQueryResult());
  }

  @Override
  public boolean save(final Group group) {
    final String query =
        "INSERT INTO " + TABLE_NAME + "(codComitiva, numPersone)" + "VALUES (?, ?)";
    final Object[] params = {group.getGroupId(), group.getNumberOfPeople()};
    return this.queryParser.computeSqlQuery(query, params);
  }

  @Override
  public boolean delete(final String primaryKey) {
    final String query = "DELETE FROM " + TABLE_NAME + " WHERE codComitiva = ?";
    final String[] params = {primaryKey};
    return this.queryParser.computeSqlQuery(query, params);
  }

  @Override
  public boolean update(final Group group) {
    final String query =
        "UPDATE " + TABLE_NAME + " SET " + "numPersone = ? " + "WHERE codComitiva = ?";
    final Object[] params = {group.getNumberOfPeople(), group.getGroupId()};
    return this.queryParser.computeSqlQuery(query, params);
  }

  private List<Group> getGroupsFromQueryResult(final QueryResult result) {
    if (result.getResult().isEmpty()) {
      return Collections.emptyList();
    }
    List<Group> groups = new ArrayList<>();
    result
        .getResult()
        .get()
        .forEach(
            row -> {
              row.forEach(System.out::println);
              final String groupId =
                  (String) this.getValueFromColumnName(row, t -> t.getKey().equals("codComitiva"));
              final int numberOfPeople =
                  (int) this.getValueFromColumnName(row, t -> t.getKey().equals("numPersone"));
              groups.add(new Group(groupId, numberOfPeople));
            });
    return groups;
  }

  private Object getValueFromColumnName(
      final List<Pair<String, Object>> row, Predicate<Pair<String, Object>> predicate) {
    return row.stream().filter(t -> predicate.test(t)).findAny().get().getValue();
  }
}
