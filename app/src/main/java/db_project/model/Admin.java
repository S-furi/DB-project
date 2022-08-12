package db_project.model;

import java.util.Date;
// Amministratore
public class Admin {
  private String id;
  private Date contractYear;
  private String firstName;
  private String lastName;
  private String telephone;
  private String email;
  private String residence;

  public Admin(
      final String id,
      final Date contractYear,
      final String firstName,
      final String lastName,
      final String telephone,
      final String email,
      final String residence) {
    this.id = id;
    this.contractYear = contractYear;
    this.firstName = firstName;
    this.lastName = lastName;
    this.telephone = telephone;
    this.email = email;
    this.residence = residence;
  }

  public Admin() {}

  public String getId() {
    return id;
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

  public void setId(final String id) {
    this.id = id;
  }

  public void setContractYear(final Date contractYear) {
    this.contractYear = contractYear;
  }

  public void setFirstName(final String firstName) {
    this.firstName = firstName;
  }

  public void setLastName(final String lastName) {
    this.lastName = lastName;
  }

  public void setTelephone(final String telephone) {
    this.telephone = telephone;
  }

  public void setEmail(final String email) {
    this.email = email;
  }

  public void setResidence(final String residence) {
    this.residence = residence;
  }

  @Override
  public String toString() {
    return String.format(
        "(%s, %s, %s, %s, %s, %s, %s)",
        this.id,
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
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
    result = prime * result + ((residence == null) ? 0 : residence.hashCode());
    result = prime * result + ((telephone == null) ? 0 : telephone.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Admin other = (Admin) obj;
    if (contractYear == null) {
      if (other.contractYear != null)
        return false;
    } else if (!contractYear.equals(other.contractYear))
      return false;
    if (email == null) {
      if (other.email != null)
        return false;
    } else if (!email.equals(other.email))
      return false;
    if (firstName == null) {
      if (other.firstName != null)
        return false;
    } else if (!firstName.equals(other.firstName))
      return false;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    if (lastName == null) {
      if (other.lastName != null)
        return false;
    } else if (!lastName.equals(other.lastName))
      return false;
    if (residence == null) {
      if (other.residence != null)
        return false;
    } else if (!residence.equals(other.residence))
      return false;
    if (telephone == null) {
      if (other.telephone != null)
        return false;
    } else if (!telephone.equals(other.telephone))
      return false;
    return true;
  }

  
}
