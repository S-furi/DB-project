package db_project.model;

import java.sql.Date;

// Sottoscrizione
public class Subscription {

  private final String passengerCode;
  private final String cardNumber;
  private final Date subscriptionDate;

  public Subscription(String passengerCode, String cardNumber, Date subscriptionDate) {
    this.passengerCode = passengerCode;
    this.cardNumber = cardNumber;
    this.subscriptionDate = subscriptionDate;
  }

  public String getPassengerCode() {
    return passengerCode;
  }

  public String getCardNumber() {
    return cardNumber;
  }

  public Date getSubscriptionDate() {
    return subscriptionDate;
  }

  @Override
  public String toString() {
    return "Subscription [cardNumber=" + cardNumber + ", subscriptionDate=" + subscriptionDate + ", passengerCode="
        + passengerCode + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((cardNumber == null) ? 0 : cardNumber.hashCode());
    result = prime * result + ((subscriptionDate == null) ? 0 : subscriptionDate.hashCode());
    result = prime * result + ((passengerCode == null) ? 0 : passengerCode.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    Subscription other = (Subscription) obj;
    if (cardNumber == null) {
      if (other.cardNumber != null) return false;
    } else if (!cardNumber.equals(other.cardNumber)) return false;
    if (subscriptionDate == null) {
      if (other.subscriptionDate != null) return false;
    } else if (!subscriptionDate.equals(other.subscriptionDate)) return false;
    if (passengerCode == null) {
      if (other.passengerCode != null) return false;
    } else if (!passengerCode.equals(other.passengerCode)) return false;
    return true;
  }
}
