package db_project.model;

import java.util.Date;
// Amministratore
public class Admin {
  private String id;
  private Date contractYear;
  private String firstName;
  private String lastName;
  private int telephone;
  private String email;
  private String residence;

  public Admin(
      final String id,
      final Date contractYear,
      final String firstName,
      final String lastName,
      final int telephone,
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

  public int getTelephone() {
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
  
  public void setTelephone(final int telephone) {
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
        "(%d, %s, %s, %s, %d, %s, %s)",
        this.id,
        this.firstName,
        this.lastName,
        this.contractYear.toString(),
        this.telephone,
        this.email,
        this.residence);
  }
}
