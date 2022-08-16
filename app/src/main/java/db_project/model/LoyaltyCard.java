package db_project.model;

// Loyalty Card
public class LoyaltyCard {
  private final String cardId;
  // maybe thoose two fields should not be final
  private final int points;
  private final int discountPercentage;

  public LoyaltyCard(final String cardId, final int points, final int discountPercentage) {

    this.cardId = cardId;
    this.points = points;
    this.discountPercentage = discountPercentage;
  }

  public String getCardId() {
    return cardId;
  }

  public int getPoints() {
    return points;
  }

  public int getDiscountPercentage() {
    return discountPercentage;
  }

  @Override
  public String toString() {
    return String.format(
        "ID: %s with %d points lead to a discount percentage of: %d",
        this.cardId, this.points, this.discountPercentage);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((cardId == null) ? 0 : cardId.hashCode());
    result = prime * result + discountPercentage;
    result = prime * result + points;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    LoyaltyCard other = (LoyaltyCard) obj;
    if (cardId == null) {
      if (other.cardId != null) return false;
    } else if (!cardId.equals(other.cardId)) return false;
    if (discountPercentage != other.discountPercentage) return false;
    if (points != other.points) return false;
    return true;
  }
}
