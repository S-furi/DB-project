package db_project.model;

// Viaggiatore
public class Traveler {

  private final String travelerCode;
  private final String firstName;
  private final String lastName;
  private final int phone;
  private final String email;
  private final String residence;
  private final String isGroup;

  public Traveler(
      String travelerCode,
      String firstName,
      String lastName,
      int phone,
      String email,
      String residence,
      String isGroup) {
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

  public int getPhone() {
    return phone;
  }

  public String getResidence() {
    return residence;
  }

  public String isGroup() {
    return isGroup;
  }

  public String getEmail() {
    return this.email;
  }
}
