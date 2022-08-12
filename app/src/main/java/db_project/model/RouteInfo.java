package db_project.model;

import java.sql.Date;

// Percorrenza
public class RouteInfo {
  public final String pathId;
  public final String trainId;
  public final Date date;

  public RouteInfo(final String pathId, final String trainId, final Date date) {
    this.pathId = pathId;
    this.trainId = trainId;
    this.date = date;
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

  @Override
  public String toString() {
    return String.format(
        "Path: %s - Train: %s - Scheduled: %s", this.pathId, this.trainId, this.date.toString());
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
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
