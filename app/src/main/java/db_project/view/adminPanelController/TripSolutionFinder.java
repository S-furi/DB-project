package db_project.view.adminPanelController;

import java.util.List;
import java.util.stream.Collectors;

import db_project.model.PathInfo;
import db_project.utils.AbstractJsonReader;

public class TripSolutionFinder {
    
    public static List<PathInfo> getPathInfosFromPathId(final String pathId) {
        return  getAllPathInfo()
                .stream()
                .filter(t -> t.getPathId().equals(pathId))
                .sorted((t1, t2) -> Integer.compare(Integer.parseInt(t1.getOrderNumber()), Integer.parseInt(t2.getOrderNumber())))
                .collect(Collectors.toList());
        
    }

    private static List<PathInfo> getAllPathInfo() {
        return new AbstractJsonReader<PathInfo>() {}.setFileName("DbPathInfoAll.json")
                .retreiveData(PathInfo.class);
    }
}
