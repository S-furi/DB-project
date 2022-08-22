package db_project.model;
// Viaggiatore
public class Passenger {

  private final String passengerCode;
  private final String firstName;
  private final String lastName;
  private final String phone;
  private final String email;
  private final String residence;

  public Passenger(
      String passengerCode,
      String firstName,
      String lastName,
      String phone,
      String email,
      String residence) {
    this.passengerCode = passengerCode;
    this.firstName = firstName;
    this.lastName = lastName;
    this.phone = phone;
    this.residence = residence;
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

  public String getEmail() {
    return this.email;
  }

  @Override
  public String toString() {
    return "Passenger [email=" + email + ", firstName=" + firstName + ", lastName=" + lastName + ", passengerCode="
        + passengerCode + ", phone=" + phone + ", residence=" + residence + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((email == null) ? 0 : email.hashCode());
    result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
    result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
    result = prime * result + ((passengerCode == null) ? 0 : passengerCode.hashCode());
    result = prime * result + ((phone == null) ? 0 : phone.hashCode());
    result = prime * result + ((residence == null) ? 0 : residence.hashCode());
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
    Passenger other = (Passenger) obj;
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
    if (lastName == null) {
      if (other.lastName != null)
        return false;
    } else if (!lastName.equals(other.lastName))
      return false;
    if (passengerCode == null) {
      if (other.passengerCode != null)
        return false;
    } else if (!passengerCode.equals(other.passengerCode))
      return false;
    if (phone == null) {
      if (other.phone != null)
        return false;
    } else if (!phone.equals(other.phone))
      return false;
    if (residence == null) {
      if (other.residence != null)
        return false;
    } else if (!residence.equals(other.residence))
      return false;
    return true;
  }

  
}
