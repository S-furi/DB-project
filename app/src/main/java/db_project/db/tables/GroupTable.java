package db_project.db.tables;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

import db_project.db.Table;
import db_project.db.queryUtils.ArrayQueryParser;
import db_project.db.queryUtils.QueryParser;
import db_project.model.Group;

public class GroupTable implements Table<Group, String> {
    public static final String TABLE_NAME = "COMITIVA";
    
    private final Connection connection;
    private final QueryParser queryParser;

    public GroupTable(final Connection connection) {
        this.connection = connection;
        this.queryParser = new ArrayQueryParser(connection);
    }

    @Override
    public String getTableName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<Group> findByPrimaryKey(String primaryKey) {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    @Override
    public List<Group> findAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean save(Group value) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean update(Group updatedValue) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean delete(String primaryKey) {
        // TODO Auto-generated method stub
        return false;
    }
}
