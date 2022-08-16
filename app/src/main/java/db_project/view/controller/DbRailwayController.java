package db_project.view.controller;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import db_project.db.dbGenerator.DBGenerator;
import db_project.db.queryUtils.ArrayQueryParser;
import db_project.db.queryUtils.QueryParser;
import db_project.db.queryUtils.QueryResult;
// import db_project.db.tables.PathInfoTable;
// import db_project.db.tables.SectionTable;
import db_project.db.tables.PathTable;
import db_project.model.Path;
import db_project.view.viewUtils.Cities;

import javafx.util.Pair;;

public class DbRailwayController {
    private final DBGenerator dbGenerator;
    // private PathInfoTable pathInfoTable;
    // private SectionTable sectionTable;
    private PathTable pathTable;

    private final QueryParser parser;

    private final Logger logger;

    public DbRailwayController(final DBGenerator dbGenerator) {
        this.dbGenerator = dbGenerator;
        this.initializeDb();
        this.parser = new ArrayQueryParser(dbGenerator.getConnectionProvider().getMySQLConnection());

        this.logger = Logger.getLogger("RailwayController");
        this.logger.setLevel(Level.INFO);
    }

    private void initializeDb() {
        this.dbGenerator.createDB();
        this.dbGenerator.createTables();
        this.pathTable = (PathTable) this.dbGenerator.getTableByClass(PathTable.class);
        // this.pathInfoTable = (PathInfoTable) this.dbGenerator.getTableByClass(PathInfoTable.class);
        // this.sectionTable = (SectionTable) this.dbGenerator.getTableByClass(SectionTable.class);
    }

    /**
     * Get the available trip solution for the given stations.
     * The result is a TripSolution, having inside it the 
     * names of the stations, the duration of the trip and
     * the total distance.
     * 
     * @param srcStation (in the value of Cities.CITY.getStationName())
     * @param dstStation (in the value of Cities.CITY.getStationName())
     * @return A TripSolution for the given input stations
     */
    public Optional<TripSolution> getTripSolution(final String srcStation, final String dstStation) {
        final Optional<Path> path = retreivePath(srcStation, dstStation);
        if (path.isEmpty()) {
            this.logger.info("Query returned a null path for: " + srcStation + "-" + dstStation);
            return Optional.empty();
        }
        return this.getTripSolutionForGivenPath(path.get());
    }

    private Optional<Path> retreivePath(final String srcStation, final String dstStation) {
        final var pathId = this.getPathCodeFromStationNames(srcStation, dstStation);
        if (pathId == null) {
            logger.info("Cannot find pathID for " + srcStation + "-" + dstStation);
            return Optional.empty();
        }
        logger.info(pathId + "...searching...");
        return this.pathTable.findByPrimaryKey(pathId);
    }

    private Optional<TripSolution> getTripSolutionForGivenPath(final Path path) {
        final String query = 
            "SELECT p.codPercorso, p.tempoTotale, SUM(t.distanza) AS Distanza "
                +"FROM percorso p, dettaglio_percorso dp, tratta t "
                +"WHERE p.codPercorso = ? "
                +"AND p.codPercorso = dp.codPercorso "
                +"AND dp.codTratta = t.codTratta "
                +"GROUP BY p.codPercorso, p.tempoTotale; ";
        final Object[] params = {path.getPathCode()};
        if (!this.parser.computeSqlQuery(query, params)) {
            logger.info("porco dio non va un cazzo");
        }
        return this.getTripSolutionsFromQuery(parser.getQueryResult()).stream().findAny();
    }

    private String getPathCodeFromStationNames(final String srcStation, final String dstStation) {
        final var src = this.getCities()
            .stream()
            .filter(t -> t.getStationName().equals(srcStation))
            .findAny();
        final var dst = this.getCities()
            .stream()
            .filter(t -> t.getStationName().equals(dstStation))
            .findAny();
        if (src.isEmpty() || dst.isEmpty()) {
            this.logger.info("One of the stations is not recognized...");
            return null;
        }
        return  src.get().getInitial() + "-" + dst.get().getInitial();
    }

    private List<TripSolution> getTripSolutionsFromQuery(final QueryResult result) {
        if (result.getResult().isEmpty()) {
            logger.info("Query Result is empty");
            return Collections.emptyList();
        }
        final List<TripSolution> tripSolutions = new ArrayList<>();
        result
            .getResult()
            .get()
            .forEach(row -> {
                logger.info(row.toString());
                final String pathId =  (String) row.get("codPercorso");
                final String duration = (String) row.get("tempoTotale");
                final var toConvert =  (BigDecimal) row.get("Distanza");
                final int distance = toConvert.intValue();
                
                tripSolutions.add(this.createTripSolution(pathId, duration, distance));
            });
        return tripSolutions;
    }

    private TripSolution createTripSolution(final String pathId, final String duration, final int distance) {
        final var stations = this.getStationsNameFromPathCode(pathId);
        final var srcStation = stations.getKey();
        final var dstStation = stations.getValue();
        return new TripSolution(srcStation, dstStation, duration, distance);
    }

    private Pair<String, String> getStationsNameFromPathCode(final String pathCode) {
        final String srcInitial = pathCode.split("-")[0];
        final String dstInitial = pathCode.split("-")[1];

        final Optional<String> src = this.getCities()
            .stream()
            .filter(t -> t.getInitial().equals(srcInitial))
            .map(t -> t.getStationName())
            .findAny();
        
        final Optional<String> dst = this.getCities()
            .stream()
            .filter(t -> t.getInitial().equals(dstInitial))
            .map(t -> t.getStationName())
            .findAny();
        if (src.isEmpty() || dst.isEmpty()) {
            throw new IllegalStateException("CANNOT RETREIVE STATIONS FROM PATHCODE!");
        }
        return new Pair<String,String>(src.get(), dst.get());
    }

    public List<String> getStations() {
        return this.getCities()
            .stream()
            .map(Cities::getStationName)
            .collect(Collectors.toList());
    }

    private List<Cities> getCities() {
        return List.of(
                Cities.BARI_CENTRALE,
                Cities.BARLETTA,
                Cities.BENEVENTO,
                Cities.BERGAMO,
                Cities.BOLOGNA,
                Cities.BOLZANO,
                Cities.BRESCIA,
                Cities.CASERTA,
                Cities.DESENZANO,
                Cities.FERRARA,
                Cities.FIRENZE,
                Cities.FOGGIA,
                Cities.GENOVA,
                Cities.LAMEZIA,
                Cities.MILANO,
                Cities.NAPOLI,
                Cities.PADOVA,
                Cities.PESCHIERA,
                Cities.REGGIO_CALABRIA,
                Cities.REGGIO_EMILIA,
                Cities.ROMA,
                Cities.ROVIGO,
                Cities.SALERNO,
                Cities.TORINO,
                Cities.TRENTO,
                Cities.TREVISO,
                Cities.TRIESTE,
                Cities.UDINE,
                Cities.VENEZIA,
                Cities.VERONA,
                Cities.VICENZA);
    }

    @SuppressWarnings("unused")
    public class TripSolution {
        private String srcStation;
        private String dstStation;
        private String duration;
        private int distance;
        
        public TripSolution(String srcStation, String dstStation, String duration, int distance) {
            this.srcStation = srcStation;
            this.dstStation = dstStation;
            this.duration = duration;
            this.distance = distance;
        }

        public String getSrcStation() {
            return srcStation;
        }

        public String getDstStation() {
            return dstStation;
        }

        public String getDuration() {
            return duration;
        }

        public int getDistance() {
            return distance;
        }

        public void setSrcStation(final String srcStation) {
            this.srcStation = srcStation;
        }

        public void setDstStation(final String dstStation) {
            this.dstStation = dstStation;
        }

        public void setDuration(final String duration) {
            this.duration = duration;
        }

        public void setDistance(int distance) {
            this.distance = distance;
        }

        @Override
        public String toString() {
            return "TripSolution [distance=" + distance + ", dstStation=" + dstStation + ", duration=" + duration
                    + ", srcStation=" + srcStation + "]";
        }       
    }
}

