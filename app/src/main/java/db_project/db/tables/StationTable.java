package db_project.db.tables;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import db_project.db.Table;
import db_project.db.queryUtils.ArrayQueryParser;
import db_project.db.queryUtils.QueryParser;
import db_project.db.queryUtils.QueryResult;
import db_project.model.Station;

public class StationTable implements Table<Station, String>{

    public final String TABLE_NAME = "STAZIONE";

    private final Connection connection;
    private final QueryParser queryParser;

    public StationTable(final Connection connection){
        this.connection = connection;
        this.queryParser = new ArrayQueryParser(this.connection);
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public Optional<Station> findByPrimaryKey(String primaryKey) {
        final String query = "SELECT * FROM " + TABLE_NAME + " WHERE codStazione = ?";
        final String[] params = {primaryKey};
        this.queryParser.computeSqlQuery(query, params);
        return getStationFromQueryResult(this.queryParser.getQueryResult()).stream().findAny();
    }

    @Override
    public List<Station> findAll() {
        final String query = "SELECT * FROM " + TABLE_NAME;
        this.queryParser.computeSqlQuery(query, null);
        return this.getStationFromQueryResult(this.queryParser.getQueryResult()); 
    }

    @Override
    public boolean save(Station station) {
        final String query = "INSERT INTO " + TABLE_NAME + "(codStazione, nome, numbinari, codResponsabile)" + " VALUES (?, ?, ?, ?)";
        final Object[] params = {
            station.getStationCode(), 
            station.getStationName(), 
            station.getRails(), 
            station.getManagerCode()
        };
        return this.queryParser.computeSqlQuery(query, params);
    }

    @Override
    public boolean update(Station updatedStation) {
        final String query = "UPDATE " + " SET " + " codResponsabile = ? WHERE codStazione = ?";
        final Object[] params = {updatedStation.getManagerCode(), updatedStation.getStationCode()};
        return this.queryParser.computeSqlQuery(query, params);
    }

    @Override
    public boolean delete(String primaryKey) {
        final String query = "DELETE FROM " + TABLE_NAME + " WHERE codStazione = ?";
        final Object[] params = {primaryKey};
        return this.queryParser.computeSqlQuery(query, params);
    }
    
    private List<Station> getStationFromQueryResult(final QueryResult result){
        if(result.getResult().isPresent()){
            return Collections.emptyList();
        }
        List<Station> station = new ArrayList<>();
        result.getResult().get().forEach(row -> {
            System.out.println(row.toString());
            final String stationCode = (String) row.get("codStazione");
            final String stationName = (String) row.get("nome");
            final int rails = (int) row.get("numBinari");
            final String managerCode = (String) row.get("codResponsabile");
            station.add(new Station(stationCode, stationName, rails, managerCode));
        });
        return station;
    }


}
