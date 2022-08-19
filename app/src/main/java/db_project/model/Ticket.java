package db_project.model;

import java.sql.Date;
import java.util.Optional;

// Biglietto
public class Ticket {
  private final String ticketId;
  private final boolean isRv;
  private final Optional<String> groupId;
  private final String passengerId;
  private final String pathId;
  private final String trainId;
  private final Date date;
  private final Float price;

  public Ticket(
      final String ticketId,
      final boolean isRv,
      final Optional<String> groupId,
      final String passengerId,
      final String pathId,
      final String trainId,
      final Date date,
      final Float price) {

    this.ticketId = ticketId;
    this.isRv = isRv;
    this.groupId = groupId;
    this.passengerId = passengerId;
    this.pathId = pathId;
    this.trainId = trainId;
    this.date = date;
    this.price = price;
  }

  public Ticket(
      final String ticketId,
      final boolean isRv,
      final Optional<String> groupId,
      final String passengerId,
      final Float price,
      final RouteInfo routeInfo) {

    this.ticketId = ticketId;
    this.isRv = isRv;
    this.groupId = groupId;
    this.passengerId = passengerId;
    this.pathId = routeInfo.getPathId();
    this.trainId = routeInfo.getTrainId();
    this.date = routeInfo.getDate();
    this.price = price;
  }

  /**
   * @return the primaryKey
   */
  public String getTicketId() {
    return ticketId;
  }

  public boolean getIsRv() {
    return isRv;
  }

  public Optional<String> getGroupId() {
    return groupId;
  }

  public String getPassengerId() {
    return passengerId;
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

  public Float getPrice() {
    return price;
  }

  @Override
  public String toString() {
    return String.format(
        "(%s) - RV: %s - Group: %s - Passenger: %s - Path: %s - Train: %s - Scheduled: %s",
        this.ticketId,
        this.isRv,
        this.groupId.isEmpty() ? "None" : this.groupId.get(),
        this.passengerId,
        this.pathId,
        this.trainId,
        this.date);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((date == null) ? 0 : date.hashCode());
    result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
    result = prime * result + (isRv ? 1231 : 1237);
    result = prime * result + ((passengerId == null) ? 0 : passengerId.hashCode());
    result = prime * result + ((pathId == null) ? 0 : pathId.hashCode());
    result = prime * result + ((price == null) ? 0 : price.hashCode());
    result = prime * result + ((ticketId == null) ? 0 : ticketId.hashCode());
    result = prime * result + ((trainId == null) ? 0 : trainId.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    Ticket other = (Ticket) obj;
    if (date == null) {
      if (other.date != null) return false;
    } else if (!date.equals(other.date)) return false;
    if (groupId == null) {
      if (other.groupId != null) return false;
    } else if (!groupId.equals(other.groupId)) return false;
    if (isRv != other.isRv) return false;
    if (passengerId == null) {
      if (other.passengerId != null) return false;
    } else if (!passengerId.equals(other.passengerId)) return false;
    if (pathId == null) {
      if (other.pathId != null) return false;
    } else if (!pathId.equals(other.pathId)) return false;
    if (price == null) {
      if (other.price != null) return false;
    } else if (!price.equals(other.price)) return false;
    if (ticketId == null) {
      if (other.ticketId != null) return false;
    } else if (!ticketId.equals(other.ticketId)) return false;
    if (trainId == null) {
      if (other.trainId != null) return false;
    } else if (!trainId.equals(other.trainId)) return false;
    return true;
  }
}
