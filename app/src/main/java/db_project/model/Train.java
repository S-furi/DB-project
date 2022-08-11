package db_project.model;

// Treno
public class Train {

  private final String trainCode;
  private final String licenseNumber;
  private final int capacity;
  private final boolean isRv;

  public Train(String trainCode, String licenseNumber, int capacity, boolean isRv) {
    this.trainCode = trainCode;
    this.licenseNumber = licenseNumber;
    this.capacity = capacity;
    this.isRv = isRv;
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

  public boolean isRv() {
    return isRv;
  }

  @Override
  public String toString() {
    return "Train [capacity="
        + capacity
        + ", isRv="
        + isRv
        + ", licenseNumber="
        + licenseNumber
        + ", trainCode="
        + trainCode
        + "]";
  }
}
