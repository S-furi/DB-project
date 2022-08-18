package db_project.db;

import java.util.List;

public interface JsonReadeable<T> {
  List<T> readFromFile();

  boolean saveToDb();
}
