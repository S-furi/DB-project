package db_project.model;

import java.sql.Date;

// Amministratore Stazione
public class StationManager {

  private final String managerCode;
  private final Date contractYear;
  private final String firstName;
  private final String lastName;
  private final int phone;
  private final String email;
  private final String residence;

  public StationManager(
      String managerCode,
      Date contractYear,
      String firstName,
      String lastName,
      int phone,
      String email,
      String residence) {
    this.managerCode = managerCode;
    this.contractYear = contractYear;
    this.firstName = firstName;
    this.lastName = lastName;
    this.phone = phone;
    this.email = email;
    this.residence = residence;
  }

  public String getManagerCode() {
    return managerCode;
  }

  public Date getContractYear() {
    return contractYear;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public int getPhone() {
    return phone;
  }

  public String getResidence() {
    return residence;
  }

  public String getEmail() {
    return this.email;
  }

  @Override
  public String toString() {
    return "StationManager [contractYear="
        + contractYear
        + ", firstName="
        + firstName
        + ", lastName="
        + lastName
        + ", managerCode="
        + managerCode
        + ", phone="
        + phone
        + ", residence="
        + residence
        + "]";
  }
}
