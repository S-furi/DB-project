package db_project.utils;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

public class SampleCitiesReader {

  private final String filename;

  public SampleCitiesReader(String filename) {
    this.filename = filename;
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
}
