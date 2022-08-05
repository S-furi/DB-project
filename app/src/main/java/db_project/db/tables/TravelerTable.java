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
import db_project.model.Traveler;

public class TravelerTable implements Table<Traveler, String>{

    public static final String TABLE_NAME = "TRAVELER";

    private final Connection connection;
    private final QueryParser queryParser;

    public TravelerTable(final Connection connection){
        this.connection = connection;
        this.queryParser = new ArrayQueryParser(this.connection);
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public Optional<Traveler> findByPrimaryKey(String primaryKey) {
        final String query = "SELECT * FROM " + TABLE_NAME + " WHERE travelerCode = ?";
        final String[] params = {primaryKey};
        this.queryParser.computeSqlQuery(query, params);
        return this.getTravelersFromQueryResult(this.queryParser.getQueryResult()).stream().findAny();
    }

    @Override
    public List<Traveler> findAll() {
        final String query = "SELECT * FROM " + TABLE_NAME;
        this.queryParser.computeSqlQuery(query, null);
        return this.getTravelersFromQueryResult(this.queryParser.getQueryResult());
    }

    @Override
    public boolean save(Traveler traveler) {
        final String query = "INSERT INTO " + TABLE_NAME + "(travelerCode, firstName, lastName, phone, residence, isGroup)" + "VALUES (?, ?, ?, ?, ?, ?)";
        final Object[] params = {
            traveler.getTravelerCode(), traveler.getFirstName(), traveler.getLastName(), traveler.getPhone(), traveler.getResidence(), traveler.isGroup()
        };
        return this.queryParser.computeSqlQuery(query, params);
    }

    @Override
    public boolean update(Traveler updatedTraveler) {
        final String query = "UPDATE " + TABLE_NAME + " SET " + " isGroup = ? WHERE travelerCode = ?";
        final Object[] params = {
            updatedTraveler.isGroup(), updatedTraveler.getTravelerCode()
        };
        return this.queryParser.computeSqlQuery(query, params);
    }

    @Override
    public boolean delete(String primaryKey) {
        final String query = "DELETE FROM " + TABLE_NAME + " WHERE travelerCode = ?";
        final Object[] params = {primaryKey};
        return this.queryParser.computeSqlQuery(query, params);
    }
    

    private List<Traveler> getTravelersFromQueryResult(final QueryResult result){
        if (result.getResult().isEmpty()){
            return Collections.emptyList();
        }
        List<Traveler> travelers = new ArrayList<>();
        result.getResult().get().forEach(row -> {
            System.out.println(row.toString());
            final String travelerCode = (String) row.get("travelerCode");
            final String firstName = (String) row.get("firstName");
            final String lastName = (String) row.get("lastName");
            final int phone = (int) row.get("phone");
            final String residence = (String) row.get("residence");
            final boolean isGroup = (boolean) row.get("isGroup");
            travelers.add(new Traveler(travelerCode, firstName, lastName, phone, residence, isGroup));
        });
        return travelers;
    }
}
