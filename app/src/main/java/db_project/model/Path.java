package db_project.model;

// Percorso
public class Path {

    private final String pathCode;
    private final String totalTime;
    private final int stops;
    private final String adminID;

    public Path(String pathCode, String totalTime, int stops, String adminID){
        this.pathCode = pathCode;
        this.totalTime = totalTime;
        this.stops = stops;
        this.adminID = adminID;
    }

    public String getPathCode() {
        return pathCode;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public int getStops() {
        return stops;
    }

    public String getAdminID() {
        return adminID;
    }

}
