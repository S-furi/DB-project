package db_project.db;

import java.util.List;
import java.util.Optional;

/**
 * Represents a DB table, with rows represented by objects of type V and primary key of type K.
 *
 * @param V the type of the objects saved in the table
 * @param K the type of the primary key of the table
 */
public interface Table<V, K> {
  /**
   * @return the name of the table
   */
  String getTableName();

  /**
   * Finds an objet in the table with the given primary key
   *
   * @param primaryKey
   * @return an empty {@link java.util.Optional} if there is no object with the given ID in the DB.
   */
  Optional<V> findByPrimaryKey(final K primaryKey);

  /**
   * @return a list with all the rows of the DB
   */
  List<V> findAll();

  /**
   * Saves an object in the DB
   *
   * @param value
   * @return
   */
  boolean save(final V value);

  /**
   * Updates an object
   *
   * @param updatedValue
   * @return false if the object could not be updated
   */
  boolean update(final V updatedValue);

  /**
   * Deletes from the DB the row with the given primary key
   *
   * @param primaryKey
   * @return false if the row could not be deleted
   */
  boolean delete(final K primaryKey);

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

  boolean isCreated();

  void setAlreadyCreated();
}
