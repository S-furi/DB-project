package db_project.model;

import java.util.Date;
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

  public Ticket(
      final String ticketId,
      final boolean isRv,
      final Optional<String> groupId,
      final String passengerId,
      final String pathId,
      final String trainId,
      final Date date) {

    this.ticketId = ticketId;
    this.isRv = isRv;
    this.groupId = groupId;
    this.passengerId = passengerId;
    this.pathId = pathId;
    this.trainId = trainId;
    this.date = date;
  }

  public String getTicketId() {
    return ticketId;
  }

  public boolean isRv() {
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
}
