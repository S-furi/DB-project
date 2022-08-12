package db_project.model;

// Percorso
public class Path {

  private final String pathCode;
  private final String totalTime;
  private final int stops;
  private final String adminID;

  public Path(String pathCode, String totalTime, int stops, String adminID) {
    this.pathCode = pathCode;
    this.totalTime = totalTime;
    this.stops = stops;
    this.adminID = adminID;
  }

  public String getPathCode() {
    return pathCode;
  }

  public String getTotalTime() {
    return totalTime;
  }

  public int getStops() {
    return stops;
  }

  public String getAdminID() {
    return adminID;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((adminID == null) ? 0 : adminID.hashCode());
    result = prime * result + ((pathCode == null) ? 0 : pathCode.hashCode());
    result = prime * result + stops;
    result = prime * result + ((totalTime == null) ? 0 : totalTime.hashCode());
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
    Path other = (Path) obj;
    if (adminID == null) {
      if (other.adminID != null)
        return false;
    } else if (!adminID.equals(other.adminID))
      return false;
    if (pathCode == null) {
      if (other.pathCode != null)
        return false;
    } else if (!pathCode.equals(other.pathCode))
      return false;
    if (stops != other.stops)
      return false;
    if (totalTime == null) {
      if (other.totalTime != null)
        return false;
    } else if (!totalTime.equals(other.totalTime))
      return false;
    return true;
  }

  
}
