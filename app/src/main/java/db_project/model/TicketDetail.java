package db_project.model;

import java.sql.Date;

// Dettaglio Biglietto
public class TicketDetail {
  private final String ticketId;
  private final Date reservationDate;
  private final int trainClass;
  private final String trainId;
  private final int carNumber;
  private final int seatNumber;

  public TicketDetail(
      final String ticketId,
      final Date reservationDate,
      final int trainClass,
      final String trainId,
      final int carNumber,
      final int seatNumber) {
    this.ticketId = ticketId;
    this.reservationDate = reservationDate;
    this.trainClass = trainClass;
    this.trainId = trainId;
    this.carNumber = carNumber;
    this.seatNumber = seatNumber;
  }

  public String getTicketId() {
    return ticketId;
  }

  public Date getReservationDate() {
    return reservationDate;
  }

  public int getTrainClass() {
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
        "(%s) ReservationDate: %s - %d Class Ticket - Train: %s - CarNo: %d - Seat: %d",
        this.ticketId,
        this.reservationDate,
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
    result = prime * result + ((reservationDate == null) ? 0 : reservationDate.hashCode());
    result = prime * result + seatNumber;
    result = prime * result + ((ticketId == null) ? 0 : ticketId.hashCode());
    result = prime * result + trainClass;
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
    if (reservationDate == null) {
      if (other.reservationDate != null) return false;
    } else if (!reservationDate.equals(other.reservationDate)) return false;
    if (seatNumber != other.seatNumber) return false;
    if (ticketId == null) {
      if (other.ticketId != null) return false;
    } else if (!ticketId.equals(other.ticketId)) return false;
    if (trainClass != other.trainClass) return false;
    if (trainId == null) {
      if (other.trainId != null) return false;
    } else if (!trainId.equals(other.trainId)) return false;
    return true;
  }
}
