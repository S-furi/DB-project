package db_project.model;

// Treno
public class Train {

  private final String trainCode;
  private final String licenseNumber;
  private final int capacity;
  private final String isRegionaleVeloce;

  public Train(String trainCode, String licenseNumber, int capacity, String isRegionaleVeloce) {
    this.trainCode = trainCode;
    this.licenseNumber = licenseNumber;
    this.capacity = capacity;
    this.isRegionaleVeloce = isRegionaleVeloce;
  }

  public String getTrainCode() {
    return trainCode;
  }

  public String getLicenseNumber() {
    return licenseNumber;
  }

  public int getCapacity() {
    return capacity;
  }

  public String isRegionaleVeloce() {
    return isRegionaleVeloce;
  }

  @Override
  public String toString() {
    return "Train [capacity="
        + capacity
        + ", isRegionaleVeloce="
        + isRegionaleVeloce
        + ", licenseNumber="
        + licenseNumber
        + ", trainCode="
        + trainCode
        + "]";
  }
}
