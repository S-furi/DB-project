package db_project.model;

import java.util.Date;

// Percorrenza
public class RouteInfo {
    public final String pathId;
    public final String trainId;
    public final Date date;

    public RouteInfo(final String pathId, final String trainId, final Date date) {
        this.pathId = pathId;
        this.trainId = trainId;
        this.date = date;
    }

    public String getPathId() {
        return pathId;
    }

    public String getTrainId() {
        return trainId;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return String.format(
            "Path: %s - Train: %s - Scheduled: %s",
            this.pathId,
            this.trainId,
            this.date.toString());
    }
}
