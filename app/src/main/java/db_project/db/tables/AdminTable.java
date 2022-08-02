package db_project.db.tables;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

import db_project.db.Table;
import db_project.model.Admin;

public class AdminTable implements Table<Admin, Integer> {
    public static final String TABLE_NAME = "AMMINISTRATORE";
    private final Connection connection;

    public AdminTable(final Connection connection) {
        this.connection = connection;
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public boolean createTable() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean dropTable() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Optional<Admin> findByPrimaryKey(Integer primaryKey) {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    @Override
    public List<Admin> findAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean save(Admin value) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean update(Admin updatedValue) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean delete(Integer primaryKey) {
        // TODO Auto-generated method stub
        return false;
    }
    
}
