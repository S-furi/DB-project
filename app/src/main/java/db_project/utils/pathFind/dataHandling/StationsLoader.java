package db_project.utils.pathFind.dataHandling;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javafx.util.Pair;


public class StationsLoader {
    private static final String STATION_IDS_FILEPATH = "data_generation/railway/route_data/statID.json";
    private static final String STATION_DISTANCES_FILEPATH = "data_generation/railway/route_data/routes_distances_diff.json";
    
    private final JSONParser parser;
    private final Map<String, List<Pair<String, Double>>> stationsRouteInfo;

    public StationsLoader() {
        this.parser = new JSONParser();
        this.stationsRouteInfo = new HashMap<>();
    }

    @SuppressWarnings ("unchecked")
    public List<String> getStations() {
        var jsonData = this.getFileData(STATION_IDS_FILEPATH);
        final LinkedList<String> stations = new LinkedList<>();
        
        for(final var data : jsonData) {
            JSONObject station = (JSONObject)data;
            station.forEach((k,v) -> stations.addFirst((String)k));
        }
        return stations;
    }

    public Map<String, List<Pair<String, Double>>> getDistancesData() {
        final var jsonData = this.getFileData(STATION_DISTANCES_FILEPATH);
        final var stations = this.getStations();
        for(final var data : jsonData) {

            JSONObject src = (JSONObject)data;
            for(final String srcStation : stations) {
                System.out.println(srcStation);
                JSONArray tripSolutions = (JSONArray)src.get(srcStation);
                tripSolutions.forEach(t -> System.out.println(((JSONObject)t).toJSONString()));
                break;
            }
        }
        // for(final var data : jsonData) {
        //     JSONObject stat = (JSONObject) data;
        //     JSONArray res = (JSONArray)stat.get("VENEZIA SANTA LUCIA");

        //     // for(final var dst : res) {
        //     //     double distance = (double)((JSONObject)((JSONObject)dst).get("PADOVA")).get("distance");
        //     //     System.out.println(distance);
        //     // }
            
        //     //stat.forEach((k,v) -> this.statId.put((String)k, (String)v));
        // }
        return null;
    }

    private void addStationData(String src, String dst, double distance) {
        if(!this.stationsRouteInfo.containsKey(src)) {
            List<Pair<String, Double>> list = new LinkedList<>();
            list.add(new Pair<String, Double>(dst, distance));
            this.stationsRouteInfo.put(src, list);
        } else {
            var list = this.stationsRouteInfo.get(src);
            list.add(new Pair<String,Double>(dst, distance));
        }
    }

    private JSONArray getFileData(final String filename) {
        File file = null;
        try {
            file = this.getFileFromResources(filename);
            JSONArray jsonData = (JSONArray) this.parser.parse(new FileReader(file));
            return jsonData;
        } catch (FileNotFoundException e) {
            System.out.println(String.format("File %s not found!", filename));
            System.exit(1);
        } catch (Exception e ) {
            e.printStackTrace();
            System.exit(0);
        }
        return null;
    }

    private File getFileFromResources(final String filename) throws URISyntaxException {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = Objects.requireNonNull(classLoader.getResource(filename));
        return new File(resource.toURI());
    }
}
