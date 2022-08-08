package db_project.model;

// Classe
public class CarClass {

  private final int classType;
  private final int availableSeats;

  public CarClass(int classType, int availableSeats) {
    this.classType = classType;
    this.availableSeats = availableSeats;
  }

  public int getClassType() {
    return this.classType;
  }

  public int getAvailableSeats() {
    return this.availableSeats;
  }
}
