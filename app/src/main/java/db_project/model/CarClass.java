package db_project.model;

// Classe
public class CarClass {

  private final String classType;
  private final int availableSeats;

  public CarClass(final String classType, final int availableSeats) {
    this.classType = classType;
    this.availableSeats = availableSeats;
  }

  public String getClassType() {
    return this.classType;
  }

  public int getAvailableSeats() {
    return this.availableSeats;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + availableSeats;
    result = prime * result + ((classType == null) ? 0 : classType.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    CarClass other = (CarClass) obj;
    if (availableSeats != other.availableSeats)
      return false;
    if (classType == null) {
      if (other.classType != null)
        return false;
    } else if (!classType.equals(other.classType))
      return false;
    return true;
  }

}
