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
import db_project.model.City;

public class CityTable implements Table<City, String> {
    public static final String TABLE_NAME;
    private final Connection connection;
    private final QueryParser queryParser;
    
    public CityTable(final Connection connection) {
        this.connection = connection;
        this.queryParser = new ArrayQueryParser(this.connection);
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public Optional<City> findByPrimaryKey(final String primaryKey) {
        final String query = "SELECT * FROM " + TABLE_NAME + " WHERE nome = ?";
        final String[] params = {primaryKey};
        this.queryParser.computeSqlQuery(query, params);
        return this.getCitiesFromQueryResult(this.queryParser.getQueryResult()).stream().findAny();
    }

    @Override
    public List<City> findAll() {
        final String query = "SELECT * FROM " + TABLE_NAME;
        this.queryParser.computeSqlQuery(query, null);
        return this.getCitiesFromQueryResult(this.queryParser.getQueryResult());
    }

    @Override
    public boolean save(final City city) {
        final String query = 
            "INSERT INTO " + TABLE_NAME
            + " (nome, regione, provincia) "
            + "VALUES (?, ?, ?)";
        final Object[] params = {
            city.getName(),
            city.getRegion(),
            city.getProvince()
        };
        return this.queryParser.computeSqlQuery(query, params);
    }

    @Override
    public boolean update(final City city) {
        final String query = 
            "UPDATE " + TABLE_NAME
            + " SET "
            + " regione = ?, provincia = ? WHERE nome = ?";
        final Object[] params = {city.getRegion(), city.getProvince(), city.getName()};
        return this.queryParser.computeSqlQuery(query, params);
    }

    @Override
    public boolean delete(final String primaryKey) {
        final String query = "DELETE FROM " + TABLE_NAME + " WHERE nome = ?";
        final Object[] params = {primaryKey};
        return this.queryParser.computeSqlQuery(query, params);
    }
   
    private List<City> getCitiesFromQueryResult(final QueryResult result) {
        if (result.getResult().isEmpty()) {
            return Collections.emptyList();
        }
        List<City> cieties = new ArrayList<>();
        result
            .getResult()
            .get()
            .forEach(row -> {
               System.out.println(row.toString()); 
               final String name = (String) row.get("nome");
               final String region = (String) row.get("regione");
               final String province = (String) row.get("provincia");
               cieties.add(new City(name, region, province));
            });

        return cieties;
    }
}
