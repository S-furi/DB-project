package db_project.model;

// Tratta
public class Section {

  private final String startStation;
  private final String endStation;
  private final String sectionCode;
  private final int distance;

  public Section(String startStation, String endStation, String sectionCode, int distance) {
    this.startStation = startStation;
    this.endStation = endStation;
    this.sectionCode = sectionCode;
    this.distance = distance;
  }

  public String getStartStation() {
    return startStation;
  }

  public String getEndStation() {
    return endStation;
  }

  public String getSectionCode() {
    return sectionCode;
  }

  public int getDistance() {
    return distance;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + distance;
    result = prime * result + ((endStation == null) ? 0 : endStation.hashCode());
    result = prime * result + ((sectionCode == null) ? 0 : sectionCode.hashCode());
    result = prime * result + ((startStation == null) ? 0 : startStation.hashCode());
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
    Section other = (Section) obj;
    if (distance != other.distance)
      return false;
    if (endStation == null) {
      if (other.endStation != null)
        return false;
    } else if (!endStation.equals(other.endStation))
      return false;
    if (sectionCode == null) {
      if (other.sectionCode != null)
        return false;
    } else if (!sectionCode.equals(other.sectionCode))
      return false;
    if (startStation == null) {
      if (other.startStation != null)
        return false;
    } else if (!startStation.equals(other.startStation))
      return false;
    return true;
  }
  
}
