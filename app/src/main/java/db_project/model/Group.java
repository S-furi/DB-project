package db_project.model;

// Comitiva
public class Group {
    private final String groupId;
    private final int numberOfPeople;

    public Group(final String groupId, final int numberOfPeople) {
        this.groupId = groupId;
        this.numberOfPeople = numberOfPeople;
    }

    public String getGroupId() {
        return groupId;
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    @Override
    public String toString() {
        return String.format(
            "Gruppo: %s formato da %d persone",
            this.groupId,
            this.numberOfPeople
            );
    }
}
