package db_project.db.queryUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javafx.util.Pair;

public class ArrayQueryParser implements QueryParser{
    private final Connection connection;
    private StringBuilder finalQuery;
    private Object[] params;

    public ArrayQueryParser(final Connection connection) {
        this.connection = connection;
        this.finalQuery = new StringBuilder();
    }

    /**
     * @param query
     * @param params
     */
    public ArrayQueryParser insertQuery(final String query, final Object[] params) {
        this.finalQuery.append(query);
        this.params = params;
        return this;
    }

    @Override
    public List<List<Pair<String, Object>>> getQueryResult() {
        if (this.params == null) {
            return this.basicQuery();
        } 
        try(final var statement = connection.prepareStatement(this.finalQuery.toString())) {
            System.out.println(this.finalQuery.toString());
            for (var i = 0; i < this.params.length; i++) {
                this.setTypeStatement(statement, this.params[i], i + 1);
            }
            ResultSet rs = statement.executeQuery();
            return generateResultFromResultSet(rs);

        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private List<List<Pair<String, Object>>> basicQuery() {
        try(final var statement = connection.createStatement()) {
            System.out.println(this.finalQuery.toString());
            ResultSet rs = statement.executeQuery(this.finalQuery.toString());
            return generateResultFromResultSet(rs);

        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public boolean executeQuery() {
        try(final var statement = connection.prepareStatement(this.finalQuery.toString())) {
            System.out.println(this.finalQuery.toString());
            for (var i = 0; i < this.params.length; i++) {
                this.setTypeStatement(statement, this.params[i], i + 1);
            }
            statement.executeUpdate();
            return true;

        } catch (final SQLException e) {
            throw new IllegalStateException(e);
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

    @Override
    public String getQuery() {
        return this.finalQuery.toString();
    }

    @Override
    public void resetQuery() {
        this.finalQuery = new StringBuilder();
        
    }
    
}
