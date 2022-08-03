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
}
