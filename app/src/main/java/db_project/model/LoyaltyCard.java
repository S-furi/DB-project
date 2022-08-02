package db_project.model;

// Loyalty Card
public class LoyaltyCard {
    private final String cardId;
    //maybe thoose two fields should not be final
    private final int points;
    private final int discountPercentage;

    public LoyaltyCard(final String cardId,
        final int points, 
        final int discountPercentage) {
        
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
            this.cardId, this.points, this.discountPercentage
        );
    }
}
