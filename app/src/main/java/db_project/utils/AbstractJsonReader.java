package db_project.utils;

import java.io.File;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.reflect.TypeParameter;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class AbstractJsonReader<T> {
  private String filename;
  private boolean setUp;
  private List<T> data = new ArrayList<>();

  public List<T> retreiveData(Class<T> elementClass) {
    if (!this.setUp) {
      throw new IllegalAccessError("No filename is specified!");
    }
    try (final Reader reader =
        Files.newBufferedReader(Paths.get(this.getFileFromResources().getAbsolutePath()))) {
      final var listType =
          new TypeToken<List<T>>() {}.where(new TypeParameter<T>() {}, elementClass).getType();

      final Gson gson = new GsonBuilder().setDateFormat("yyyy-mm-dd").create();
      this.data = gson.fromJson(reader, listType);
      return this.data;

    } catch (final Exception e) {
      throw new IllegalStateException(e);
    }
  }

  private File getFileFromResources() throws URISyntaxException {
    final ClassLoader classLoader = this.getClass().getClassLoader();
    URL resouce = classLoader.getResource(this.filename);
    if (resouce == null) {
      throw new IllegalArgumentException("File \"" + this.filename + "\" not found!");
    } else {
      return new File(resouce.toURI());
    }
  }

  public List<T> getChachedData() {
    return this.data.isEmpty() ? Collections.emptyList() : this.data;
  }

  public AbstractJsonReader<T> setFileName(final String filename) {
    this.filename = "data_generation/DbData/" + filename;
    this.setUp = true;
    return this;
  }
}
