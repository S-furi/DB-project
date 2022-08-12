package db_project.model;

import java.util.Optional;

// Viaggiatore
public class Passenger {

  private final String passengerCode;
  private final String firstName;
  private final String lastName;
  private final String phone;
  private final String email;
  private final String residence;
  private final Optional<String> isGroup;

  public Passenger(
      String passengerCode,
      String firstName,
      String lastName,
      String phone,
      String email,
      String residence,
      Optional<String> isGroup) {
    this.passengerCode = passengerCode;
    this.firstName = firstName;
    this.lastName = lastName;
    this.phone = phone;
    this.residence = residence;
    this.isGroup = isGroup;
    this.email = email;
  }

  public String getPassengerCode() {
    return passengerCode;
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

  public Optional<String> isGroup() {
    return isGroup;
  }

  public String getEmail() {
    return this.email;
  }

  @Override
  public String toString() {
    return "Passenger [email="
        + email
        + ", firstName="
        + firstName
        + ", isGroup="
        + isGroup
        + ", lastName="
        + lastName
        + ", phone="
        + phone
        + ", residence="
        + residence
        + ", passengerCode="
        + passengerCode
        + "]";
  }
}
