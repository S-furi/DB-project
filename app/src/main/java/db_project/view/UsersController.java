package db_project.view;

import java.util.List;

import db_project.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class UsersController {
  private final ObservableList<User> users;

  public UsersController() {
    this.users = FXCollections.observableArrayList();
  }

  public List<TableColumn<User, ?>> getTableViewColumns() {
    TableColumn<User, String> firstNameColumn = new TableColumn<>("First Name");
    firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstname"));

    TableColumn<User, String> lastNameColumn = new TableColumn<>("Last Name");
    lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastname"));

    TableColumn<User, String> telColumn = new TableColumn<>("Tel");
    telColumn.setCellValueFactory(new PropertyValueFactory<>("tel"));

    TableColumn<User, String> emailColumn = new TableColumn<>("Email");
    emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

    return List.of(firstNameColumn, lastNameColumn, telColumn, emailColumn);
  }

  public ObservableList<User> getUsers() {
    return users;
  }

  public boolean addUser(final User user) {
    return this.users.add(user);
  }

  public boolean removeUser(final User user) {
    return this.users.remove(user);
  }
}
