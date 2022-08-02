package db_project.model;

// Dettaglio Percorso
public class PathInfo {
   private final String sectionId;
   private final int orderNumber;
   private final String pathId;

   public PathInfo(
           final String sectionId,
           final int orderNumber,
           final String pathId
           ) {
       this.sectionId = sectionId;
       this.orderNumber = orderNumber;
       this.pathId = pathId;

   }

   public String getSectionId() {
       return this.sectionId;
   }

   public int getOrderNumber() {
        return this.orderNumber;
   }

   public String getPathId() {
       return this.pathId;
   }

   @Override
   public String toString() {
       return String.format(
               "Section %s number %d of Path %s",
                this.sectionId,
                this.orderNumber,
                this.pathId
        );
   }

}
