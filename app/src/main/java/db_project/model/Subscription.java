package db_project.model;

import java.sql.Date;

// Sottoscrizione
public class Subscription {

  private final String travelerCode;
  private final String cardNumber;
  private final Date subscriptionDate;

  public Subscription(String travelerCode, String cardNumber, Date subscriptionDate) {
    this.travelerCode = travelerCode;
    this.cardNumber = cardNumber;
    this.subscriptionDate = subscriptionDate;
  }

  public String getTravelerCode() {
    return travelerCode;
  }

  public String getCardNumber() {
    return cardNumber;
  }

  public Date getSubscriptionDate() {
    return subscriptionDate;
  }
}
