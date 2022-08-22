package db_project.model;

import java.sql.Date;

// Dettaglio Biglietto
public class TicketDetail {
  private final String ticketId;
  private final Date tripDate;
  private final String trainClass;
  private final String trainId;
  private final int carNumber;
  private final int seatNumber;

  public TicketDetail(
      final String ticketId,
      final Date tripDate,
      final String trainClass,
      final String trainId,
      final int carNumber,
      final int seatNumber) {
    this.ticketId = ticketId;
    this.tripDate = tripDate;
    this.trainClass = trainClass;
    this.trainId = trainId;
    this.carNumber = carNumber;
    this.seatNumber = seatNumber;
  }

  public String getTicketId() {
    return ticketId;
  }

  public Date getTripDate() {
    return tripDate;
  }

  public String getTrainClass() {
    return trainClass;
  }

  public String getTrainId() {
    return trainId;
  }

  public int getCarNumber() {
    return carNumber;
  }

  public int getSeatNumber() {
    return seatNumber;
  }

  @Override
  public String toString() {
    return String.format(
        "(%s) Trip scheduled on: %s - %s Class Ticket - Train: %s - CarNo: %d - Seat: %d",
        this.ticketId,
        this.tripDate,
        this.trainClass,
        this.trainId,
        this.carNumber,
        this.seatNumber);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + carNumber;
    result = prime * result + ((tripDate == null) ? 0 : tripDate.hashCode());
    result = prime * result + seatNumber;
    result = prime * result + ((ticketId == null) ? 0 : ticketId.hashCode());
    result = prime * result + ((trainClass == null) ? 0 : trainClass.hashCode());
    result = prime * result + ((trainId == null) ? 0 : trainId.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    TicketDetail other = (TicketDetail) obj;
    if (carNumber != other.carNumber) return false;
    if (tripDate == null) {
      if (other.tripDate != null) return false;
    } else if (!tripDate.equals(other.tripDate)) return false;
    if (seatNumber != other.seatNumber) return false;
    if (ticketId == null) {
      if (other.ticketId != null) return false;
    } else if (!ticketId.equals(other.ticketId)) return false;
    if (trainClass == null) {
      if (other.trainClass != null) return false;
    } else if (!trainClass.equals(other.trainClass)) return false;
    if (trainId == null) {
      if (other.trainId != null) return false;
    } else if (!trainId.equals(other.trainId)) return false;
    return true;
  }

  public static TicketDetail getTicketDetailFromSeat(
      final String ticketId, final Date tripDate, final Seat seat) {
    return new TicketDetail(
        ticketId,
        tripDate,
        seat.getClassType(),
        seat.getTrainCode(),
        seat.getCarNumber(),
        seat.getSeatNumber());
  }
}
