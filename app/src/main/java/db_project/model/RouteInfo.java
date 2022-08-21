package db_project.model;

import java.sql.Date;

// Percorrenza
public class RouteInfo {
  private final String pathId;
  private final String trainId;
  private final Date date;
  private final String actualDuration;
  private final int availableSeats;

  public RouteInfo(final String pathId, final String trainId, final Date date, final String actualDuration, final int availableSeats) {
    this.pathId = pathId;
    this.trainId = trainId;
    this.date = date;
    this.actualDuration = actualDuration;
    this.availableSeats = availableSeats;
  }

  public String getPathId() {
    return pathId;
  }

  public String getTrainId() {
    return trainId;
  }

  public Date getDate() {
    return date;
  }

  public String getActualDuration() {
    return actualDuration;
  }

  public int getAvailableSeats() {
    return availableSeats;
  }

  @Override
  public String toString() {
    return "RouteInfo [actualDuration=" + actualDuration + ", availableSeats=" + availableSeats + ", date=" + date
        + ", pathId=" + pathId + ", trainId=" + trainId + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((actualDuration == null) ? 0 : actualDuration.hashCode());
    result = prime * result + availableSeats;
    result = prime * result + ((date == null) ? 0 : date.hashCode());
    result = prime * result + ((pathId == null) ? 0 : pathId.hashCode());
    result = prime * result + ((trainId == null) ? 0 : trainId.hashCode());
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
    RouteInfo other = (RouteInfo) obj;
    if (actualDuration == null) {
      if (other.actualDuration != null)
        return false;
    } else if (!actualDuration.equals(other.actualDuration))
      return false;
    if (availableSeats != other.availableSeats)
      return false;
    if (date == null) {
      if (other.date != null)
        return false;
    } else if (!date.equals(other.date))
      return false;
    if (pathId == null) {
      if (other.pathId != null)
        return false;
    } else if (!pathId.equals(other.pathId))
      return false;
    if (trainId == null) {
      if (other.trainId != null)
        return false;
    } else if (!trainId.equals(other.trainId))
      return false;
    return true;
  }
  
  
}
