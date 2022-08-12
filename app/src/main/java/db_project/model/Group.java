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
    return String.format("Gruppo: %s formato da %d persone", this.groupId, this.numberOfPeople);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
    result = prime * result + numberOfPeople;
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
    Group other = (Group) obj;
    if (groupId == null) {
      if (other.groupId != null)
        return false;
    } else if (!groupId.equals(other.groupId))
      return false;
    if (numberOfPeople != other.numberOfPeople)
      return false;
    return true;
  }
  
}
