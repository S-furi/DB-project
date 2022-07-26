package db_project.db.queryUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javafx.util.Pair;

public class MapQueryParser {
    private final Connection connection;
    private StringBuilder finalQuery;
    private final Map<Integer, Object> params;
    private int paramCount;

    public MapQueryParser(final Connection connection) {
        this.connection = connection;
        this.paramCount = 1;
        this.finalQuery = new StringBuilder();
        this.params = new HashMap<>();
    }

    public MapQueryParser select(final String query, final Object args) {
        this.insertPortion("SELECT", query, args);
        return this;
    }

    public MapQueryParser from(final String query) {
        this.insertPortion("FROM", query, "");
        return this;
    }

    public MapQueryParser where(final String query, final Object args) {
        this.insertPortion("WHERE", query, args);
        return this;
    }

    public MapQueryParser and(final String query, final Object args) {
        this.insertPortion("AND", query, args);
        return this;
    }

    /**
     * Append the new statement to the final query to be computed
     * 
     * @param op the SQL operator (e.g. SELECT, FROM, WHERE,...)
     * @param query the new portion of query to append to the current building query
     * @param args if the query has arguments marked with ?, args denote what to subistitute
     */
    private void insertPortion(final String op, final String query, final Object args) {
        this.finalQuery.append(String.format("%s %s ", op, query));
        if (!args.toString().equals("")) {
            this.params.put(this.paramCount, args);
            this.paramCount++;
        }
    }

    /**
     * Execute the created query
     * 
     * @return a List of the results, mapped as a (key,value) pair
     * where key is the name of the column, and value is the value of 
     * the i-th row of the result.
     */
    public List<List<Pair<String, Object>>> getQueryResult() {

        try(final var statement = connection.prepareStatement(this.finalQuery.toString())) {
            System.out.println(this.finalQuery.toString());
            this.params.forEach((k,v) -> {
                this.setTypeStatement(statement, v, k);
            });
            var res =  statement.executeQuery();
            return generateResultFromResultSet(res);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 
     * @param res the resultSet of a given Query
     * @return a List of the results, mapped as a (key,value) pair
     * where key is the name of the column, and value is the value of 
     * the i-th row of the result.
     */
    private List<List<Pair<String, Object>>> generateResultFromResultSet(ResultSet res) {
        List<List<Pair<String, Object>>> results = new ArrayList<>();
        try {
            var md = res.getMetaData();
            System.out.println(md.getColumnCount());
            while(res.next()) {
                final List<Pair<String, Object>> lst = new ArrayList<>();
                for (var i = 1; i <= md.getColumnCount(); i++) {
                    final String colName = md.getColumnName(i);
                    if (md.getColumnType(i) == java.sql.Types.VARCHAR) {
                        lst.add(new Pair<String, Object>(colName, res.getString(i)));
                    } else if (md.getColumnType(i) == java.sql.Types.INTEGER) {
                        lst.add(new Pair<String, Object>(colName, Objects.requireNonNull(res.getInt(i))));
                    }
                }
                results.add(lst);
            }
            return results;
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Very raw and not Object Oriented method for retreiving and using the right
     * method with preparedStatement (it's possibile also to use setObject but mmm... idk)
     * 
     * @param statement the {@link java.sql.PreparedStatement}
     * @param param
     * @param index
     */
    private void setTypeStatement(final PreparedStatement statement, Object param, int index) {
        try {
            if (param.getClass().equals(String.class)) {
                statement.setString(index, param.toString());
            } else if (param.getClass().equals(Integer.class)) {
                statement.setInt(index, (int)param);
            } else {
                throw new SQLException();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getQuery() {
        return this.finalQuery.toString();
    }

    public void resetQuery() {
        this.finalQuery = new StringBuilder();
    }
}
