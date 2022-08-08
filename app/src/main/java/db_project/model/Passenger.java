package db_project.model;

import java.util.Optional;

// Viaggiatore
public class Passenger {

  private final String travelerCode;
  private final String firstName;
  private final String lastName;
  private final String phone;
  private final String email;
  private final String residence;
  private final Optional<Object> isGroup;

  public Passenger(
      String travelerCode,
      String firstName,
      String lastName,
      String phone,
      String email,
      String residence,
      Optional<Object> isGroup) {
    this.travelerCode = travelerCode;
    this.firstName = firstName;
    this.lastName = lastName;
    this.phone = phone;
    this.residence = residence;
    this.isGroup = isGroup;
    this.email = email;
  }

  public String getTravelerCode() {
    return travelerCode;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getPhone() {
    return phone;
  }

  public String getResidence() {
    return residence;
  }

  public Optional<Object> isGroup() {
    return isGroup;
  }

  public String getEmail() {
    return this.email;
  }
}
