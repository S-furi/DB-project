package db_project.model;

import java.util.Date;

// Macchinista
public class Driver {
  private final String licenceNumber;
  private final Date contractYear;
  private final String firstName;
  private final String lastName;
  private final String telephone;
  private final String email;
  private final String residence;

  public Driver(
      final String licenceNumber,
      final Date contractYear,
      final String firstName,
      final String lastName,
      final String telephone,
      final String email,
      final String residence) {
    this.licenceNumber = licenceNumber;
    this.contractYear = contractYear;
    this.firstName = firstName;
    this.lastName = lastName;
    this.telephone = telephone;
    this.email = email;
    this.residence = residence;
  }

  public String getLicenceNumber() {
    return licenceNumber;
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

  public String getTelephone() {
    return telephone;
  }

  public String getEmail() {
    return email;
  }

  public String getResidence() {
    return residence;
  }

  @Override
  public String toString() {
    return String.format(
        "(%s, %s, %s, %s, %s, %s, %s)",
        this.licenceNumber,
        this.firstName,
        this.lastName,
        this.contractYear.toString(),
        this.telephone,
        this.email,
        this.residence);
  }
}
