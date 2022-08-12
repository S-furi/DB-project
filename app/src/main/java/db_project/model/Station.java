package db_project.model;

// Stazione
public class Station {

  private final String stationCode;
  private final String stationName;
  private final int rails;
  private final String managerCode;

  public Station(String stationCode, String stationName, int rails, String managerCode) {
    this.stationCode = stationCode;
    this.stationName = stationName;
    this.rails = rails;
    this.managerCode = managerCode;
  }

  public String getStationCode() {
    return stationCode;
  }

  public String getStationName() {
    return stationName;
  }

  public int getRails() {
    return rails;
  }

  public String getManagerCode() {
    return managerCode;
  }

  @Override
  public String toString() {
    return "Station [managerCode="
        + managerCode
        + ", rails="
        + rails
        + ", stationCode="
        + stationCode
        + ", stationName="
        + stationName
        + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((managerCode == null) ? 0 : managerCode.hashCode());
    result = prime * result + rails;
    result = prime * result + ((stationCode == null) ? 0 : stationCode.hashCode());
    result = prime * result + ((stationName == null) ? 0 : stationName.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    Station other = (Station) obj;
    if (managerCode == null) {
      if (other.managerCode != null) return false;
    } else if (!managerCode.equals(other.managerCode)) return false;
    if (rails != other.rails) return false;
    if (stationCode == null) {
      if (other.stationCode != null) return false;
    } else if (!stationCode.equals(other.stationCode)) return false;
    if (stationName == null) {
      if (other.stationName != null) return false;
    } else if (!stationName.equals(other.stationName)) return false;
    return true;
  }
}
