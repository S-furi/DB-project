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

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((contractYear == null) ? 0 : contractYear.hashCode());
    result = prime * result + ((email == null) ? 0 : email.hashCode());
    result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
    result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
    result = prime * result + ((licenceNumber == null) ? 0 : licenceNumber.hashCode());
    result = prime * result + ((residence == null) ? 0 : residence.hashCode());
    result = prime * result + ((telephone == null) ? 0 : telephone.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    Driver other = (Driver) obj;
    if (contractYear == null) {
      if (other.contractYear != null) return false;
    } else if (!contractYear.equals(other.contractYear)) return false;
    if (email == null) {
      if (other.email != null) return false;
    } else if (!email.equals(other.email)) return false;
    if (firstName == null) {
      if (other.firstName != null) return false;
    } else if (!firstName.equals(other.firstName)) return false;
    if (lastName == null) {
      if (other.lastName != null) return false;
    } else if (!lastName.equals(other.lastName)) return false;
    if (licenceNumber == null) {
      if (other.licenceNumber != null) return false;
    } else if (!licenceNumber.equals(other.licenceNumber)) return false;
    if (residence == null) {
      if (other.residence != null) return false;
    } else if (!residence.equals(other.residence)) return false;
    if (telephone == null) {
      if (other.telephone != null) return false;
    } else if (!telephone.equals(other.telephone)) return false;
    return true;
  }
}
