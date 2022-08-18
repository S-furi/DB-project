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

  public boolean getIsRv() {
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

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + capacity;
    result = prime * result + (isRv ? 1231 : 1237);
    result = prime * result + ((licenseNumber == null) ? 0 : licenseNumber.hashCode());
    result = prime * result + ((trainCode == null) ? 0 : trainCode.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    Train other = (Train) obj;
    if (capacity != other.capacity) return false;
    if (isRv != other.isRv) return false;
    if (licenseNumber == null) {
      if (other.licenseNumber != null) return false;
    } else if (!licenseNumber.equals(other.licenseNumber)) return false;
    if (trainCode == null) {
      if (other.trainCode != null) return false;
    } else if (!trainCode.equals(other.trainCode)) return false;
    return true;
  }
}
