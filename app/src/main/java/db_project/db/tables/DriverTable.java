package db_project.db.tables;

import java.util.List;
import java.util.Optional;

import db_project.db.Table;
import db_project.model.Driver;

public class DriverTable implements Table<Driver, String> {

    @Override
    public String getTableName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<Driver> findByPrimaryKey(String primaryKey) {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    @Override
    public List<Driver> findAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean save(Driver value) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean update(Driver updatedValue) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean delete(String primaryKey) {
        // TODO Auto-generated method stub
        return false;
    }
    
}
