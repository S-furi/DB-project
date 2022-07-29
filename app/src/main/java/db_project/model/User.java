package db_project.model;

import java.util.Random;

public class User {
  private final int id;
  private final String firstname;
  private final String lastname;
  private final String tel;
  private final String email;

  public User(
      final int id,
      final String firstname,
      final String lastname,
      final String telephone,
      final String email) {
    this.id = id;
    this.firstname = firstname;
    this.lastname = lastname;
    this.tel = telephone;
    this.email = email;
  }

  public static int generateId() {
    Random rand = new Random();
    return rand.ints(10000, 99999).boxed().filter(t -> t % 3 == 0).findFirst().get();
  }

  public String getUserInfo() {
    return "("
        + this.id
        + ","
        + this.firstname
        + " "
        + this.lastname
        + ", "
        + this.tel
        + ", "
        + this.email
        + ")";
  }

  public int getId() {
    return this.id;
  }

  public String getFirstname() {
    return this.firstname;
  }

  public String getLastname() {
    return this.lastname;
  }

  public String getTel() {
    return this.tel;
  }

  public String getEmail() {
    return this.email;
  }
}
