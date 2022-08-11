package db_project.db.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
// import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import db_project.model.User;
import db_project.db.Table;
// import db_project.utils.Utils;

public class UserTable implements Table<User, Integer> {
  public static final String TABLE_NAME = "users";

  private final Connection connection;

  public UserTable(final Connection connection) {
    this.connection = Objects.requireNonNull(connection);
  }

  @Override
  public String getTableName() {
    return TABLE_NAME;
  }

  public boolean createTable() {
    try (final Statement statement = this.connection.createStatement()) {
      statement.executeUpdate(
          "CREATE TABLE "
              + TABLE_NAME
              + " ("
              + "id INT NOT NULL PRIMARY KEY,"
              + "firstName CHAR(40),"
              + "lastName CHAR(40),"
              + "telephone CHAR(40),"
              + "email CHAR(40)"
              + ")");
      return true;
    } catch (final SQLException e) {
      System.out.println(e);
      return false;
    }
  }

  public boolean dropTable() {
    try (final Statement statement = this.connection.createStatement()) {
      statement.executeUpdate("DROP TABLE " + TABLE_NAME);
      return true;
    } catch (final SQLException e) {
      return false;
    }
  }

  @Override
  public Optional<User> findByPrimaryKey(final Integer id) {
    final String query = "SELECT * FROM " + TABLE_NAME + " WHERE id = ?";
    try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
      statement.setInt(1, id);
      final ResultSet resultSet = statement.executeQuery();
      return readUsersFromResultSet(resultSet).stream().findFirst();
    } catch (final SQLException e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  public List<User> findAll() {
    try (final Statement statement = this.connection.createStatement()) {
      final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TABLE_NAME);
      return readUsersFromResultSet(resultSet);
    } catch (SQLException e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  public boolean save(final User user) {
    final String query =
        "INSERT INTO "
            + TABLE_NAME
            + "(id, firstName, lastName, telephone, email) VALUES (?, ?, ?, ?, ?)";
    try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
      statement.setInt(1, user.getId());
      statement.setString(2, user.getFirstname());
      statement.setString(3, user.getLastname());
      statement.setString(4, user.getTel());
      statement.setString(5, user.getEmail());
      statement.executeUpdate();
      return true;
    } catch (final SQLIntegrityConstraintViolationException e) {
      return false;
    } catch (final SQLException e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  public boolean update(final User user) {
    final String query =
        "UPDATE "
            + TABLE_NAME
            + " SET "
            + "firstName = ?,"
            + "lastName = ?,"
            + "telephone = ?,"
            + "email = ? "
            + "WHERE id = ?";
    try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
      statement.setString(1, user.getFirstname());
      statement.setString(2, user.getLastname());
      statement.setString(3, user.getTel());
      statement.setString(4, user.getEmail());
      statement.setInt(5, user.getId());
      return statement.executeUpdate() > 0;
    } catch (SQLException e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  public boolean delete(Integer id) {
    final String query = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
    try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
      statement.setInt(1, id);
      return statement.executeUpdate() > 0;
    } catch (final SQLException e) {
      throw new IllegalStateException(e);
    }
  }

  private List<User> readUsersFromResultSet(final ResultSet resultSet) {
    final List<User> users = new ArrayList<>();
    try {
      while (resultSet.next()) {
        final int id = resultSet.getInt("id");
        final String firstName = resultSet.getString("firstName");
        final String lastName = resultSet.getString("lastName");
        final String telephone = resultSet.getString("telephone");
        final String email = resultSet.getString("email");
        // final Optional <Date> birthday =
        // Optional.ofNullable(Utils.sqlDateToDate(resultSet.getDate("birthday")));
        final User user = new User(id, firstName, lastName, telephone, email);
        users.add(user);
      }
    } catch (final SQLException e) {
    }
    return users;
  }

  @Override
  public boolean isCreated() {
    return false;
  }
}
