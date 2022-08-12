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

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + carNumber;
    result = prime * result + classType;
    result = prime * result + seatNumber;
    result = prime * result + ((trainCode == null) ? 0 : trainCode.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    Seat other = (Seat) obj;
    if (carNumber != other.carNumber) return false;
    if (classType != other.classType) return false;
    if (seatNumber != other.seatNumber) return false;
    if (trainCode == null) {
      if (other.trainCode != null) return false;
    } else if (!trainCode.equals(other.trainCode)) return false;
    return true;
  }
}
