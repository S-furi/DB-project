package db_project.model;

import java.util.Date;

// Dettaglio Biglietto
public class TicketDetail {
  private final String tickedId;
  private final Date reservationDate;
  private final int trainClass;
  private final String trainId;
  private final int carNumber;
  private final int seatNumber;

  public TicketDetail(
      final String tickedId,
      final Date reservationDate,
      final int trainClass,
      final String trainId,
      final int carNumber,
      final int seatNumber) {
    this.tickedId = tickedId;
    this.reservationDate = reservationDate;
    this.trainClass = trainClass;
    this.trainId = trainId;
    this.carNumber = carNumber;
    this.seatNumber = seatNumber;
  }

  public String getTickedId() {
    return tickedId;
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
        this.tickedId,
        this.reservationDate,
        this.trainClass,
        this.trainId,
        this.carNumber,
        this.seatNumber);
  }
}
