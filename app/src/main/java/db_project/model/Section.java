package db_project.model;

// Tratta
public class Section {

    private final String startStation;
    private final String endStation;
    private final String sectionCode;
    private final int distance;

    public Section(String startStation, String endStation, String sectionCode, int distance) {
        this.startStation = startStation;
        this.endStation = endStation;
        this.sectionCode = sectionCode;
        this.distance = distance;
    }

    public String getStartStation() {
        return startStation;
    }

    public String getEndStation() {
        return endStation;
    }

    public String getSectionCode() {
        return sectionCode;
    }

    public int getDistance() {
        return distance;
    }
}
