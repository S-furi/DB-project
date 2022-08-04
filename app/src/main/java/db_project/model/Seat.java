package db_project.model;

// Posto
public class Seat {

  private final int classType;
  private final String trainCode;
  private final int carNumber;
  private final int seatNumber;

  public Seat(int classType, String trainCode, int carNumber, int seatNumber) {
    this.classType = classType;
    this.trainCode = trainCode;
    this.carNumber = carNumber;
    this.seatNumber = seatNumber;
  }

  public int getClassType() {
    return classType;
  }

  public String getTrainCode() {
    return trainCode;
  }

  public int getCarNumber() {
    return carNumber;
  }

  public int getSeatNumber() {
    return seatNumber;
  }
}
