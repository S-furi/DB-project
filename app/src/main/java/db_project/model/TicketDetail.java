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
}
