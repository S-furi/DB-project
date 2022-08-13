package db_project.model;

// Dettaglio Percorso
public class PathInfo {
  private final String sectionId;
  private final String orderNumber;
  private final String pathId;

  public PathInfo(final String sectionId, final String orderNumber2, final String pathId) {
    this.sectionId = sectionId;
    this.orderNumber = orderNumber2;
    this.pathId = pathId;
  }

  public String getSectionId() {
    return this.sectionId;
  }

  public String getOrderNumber() {
    return this.orderNumber;
  }

  public String getPathId() {
    return this.pathId;
  }

  @Override
  public String toString() {
    return String.format(
        "Section %s number %s of Path %s", this.sectionId, this.orderNumber, this.pathId);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((orderNumber == null) ? 0 : orderNumber.hashCode());
    result = prime * result + ((pathId == null) ? 0 : pathId.hashCode());
    result = prime * result + ((sectionId == null) ? 0 : sectionId.hashCode());
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
    PathInfo other = (PathInfo) obj;
    if (orderNumber == null) {
      if (other.orderNumber != null)
        return false;
    } else if (!orderNumber.equals(other.orderNumber))
      return false;
    if (pathId == null) {
      if (other.pathId != null)
        return false;
    } else if (!pathId.equals(other.pathId))
      return false;
    if (sectionId == null) {
      if (other.sectionId != null)
        return false;
    } else if (!sectionId.equals(other.sectionId))
      return false;
    return true;
  }
}
