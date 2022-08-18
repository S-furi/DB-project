package db_project.model;

// Carrozza
public class Car {
  public static final int NUMBER_FIRST_CLASS_SEATS = 50;
  public static final int NUMBER_SECOND_CLASS_SEATS = 100;

  private final String classType;
  private final String trainCode;
  private int position; // Dovrebbe esssere numeroCarrozza ma non so cosa scrivere
  private final int seats;
  private final boolean toilet;

  public Car(String classType, String trainCode, int position, int seats, boolean toilet) {
    this.classType = classType;
    this.trainCode = trainCode;
    this.position = position;
    this.seats = seats;
    this.toilet = toilet;
  }

  public String getClassType() {
    return classType;
  }

  public String getTrainCode() {
    return trainCode;
  }

  public int getPosition() {
    return position;
  }

  public int getSeats() {
    return seats;
  }

  public boolean hasToilet() {
    return toilet;
  }

  @Override
  public String toString() {
    return "Car [classType="
        + classType
        + ", position="
        + position
        + ", seats="
        + seats
        + ", toilet="
        + toilet
        + ", trainCode="
        + trainCode
        + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((classType == null) ? 0 : classType.hashCode());
    result = prime * result + position;
    result = prime * result + seats;
    result = prime * result + (toilet ? 1231 : 1237);
    result = prime * result + ((trainCode == null) ? 0 : trainCode.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    Car other = (Car) obj;
    if (classType == null) {
      if (other.classType != null) return false;
    } else if (!classType.equals(other.classType)) return false;
    if (position != other.position) return false;
    if (seats != other.seats) return false;
    if (toilet != other.toilet) return false;
    if (trainCode == null) {
      if (other.trainCode != null) return false;
    } else if (!trainCode.equals(other.trainCode)) return false;
    return true;
  }
}
