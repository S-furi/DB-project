package db_project.model;

// Città
public class City {
  private final String name;
  private final String region;
  private final String province;

  public City(final String name, final String region, final String province) {
    this.name = name;
    this.region = region;
    this.province = province;
  }

  public String getName() {
    return name;
  }

  public String getRegion() {
    return region;
  }

  public String getProvince() {
    return province;
  }

  @Override
  public String toString() {
    return String.format("%s (%s), %s", this.name, this.province, this.region);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((province == null) ? 0 : province.hashCode());
    result = prime * result + ((region == null) ? 0 : region.hashCode());
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
    City other = (City) obj;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    if (province == null) {
      if (other.province != null)
        return false;
    } else if (!province.equals(other.province))
      return false;
    if (region == null) {
      if (other.region != null)
        return false;
    } else if (!region.equals(other.region))
      return false;
    return true;
  }

  
}
