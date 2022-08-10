package db_project.db;

import java.util.List;
import java.util.Optional;

public interface CompositeKeyTable<T, K> {
  /**
   * @return the name of the table
   */
  String getTableName();

  /**
   * Finds an objet in the table with the given primary key
   *
   * @param primaryKey the list of the composite primary key
   * @return an empty {@link java.util.Optional} if there is no object with the given ID in the DB.
   */
  Optional<T> findByPrimaryKey(final List<K> primaryKey);

  /**
   * @return a list with all the rows of the DB
   */
  List<T> findAll();

  /**
   * Saves an object in the DB
   *
   * @param value
   * @return
   */
  boolean save(final T value);

  /**
   * Updates an object
   *
   * @param updatedTalue
   * @return false if the object could not be updated
   */
  boolean update(final T updatedTalue);

  /**
   * Deletes from the DB the row with the given primary key
   *
   * @param primaryKey the list of the composite primary key
   * @return false if the row could not be deleted
   */
  boolean delete(final List<K> primaryKey);

  /**
   * Drops the current table.
   * 
   * @return true if the table is dropped, false otherwise.
   */
  boolean dropTable();

  /**
   * Creates the current table.
   * 
   * @return true if the table is created succefully, false otherwise.
   */
  boolean createTable();
}
