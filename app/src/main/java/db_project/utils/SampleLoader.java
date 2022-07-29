package db_project.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import db_project.model.User;

public class SampleLoader {

  private final String filename;
  private final JSONParser parser;

  public SampleLoader(String filename) {
    this.filename = filename;
    parser = new JSONParser();
  }

  public List<User> getStoredPeople() {
    List<User> users = new ArrayList<>();
    File file = null;
    try {
      file = this.getFileFromResource();

      JSONArray a = (JSONArray) this.parser.parse(new FileReader(file));
      for (final var elem : a) {
        JSONObject user = (JSONObject) elem;
        users.add(this.getUserFromJson(user));
      }
    } catch (URISyntaxException e) {
      e.printStackTrace();
      System.exit(1);
    } catch (FileNotFoundException e) {
      System.out.println("File not found! " + this.filename);
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
    return users;
  }

  private User getUserFromJson(JSONObject user) {
    String name = (String) user.get("name");
    String surname = (String) user.get("surname");
    // String birthdate = (String) user.get("birthdate");
    // String cf = (String) user.get("CF");
    String email = (String) user.get("email");
    String telephone = String.valueOf((Long) user.get("telephone"));
    return new User(User.generateId(), name, surname, telephone, email);
  }

  private File getFileFromResource() throws URISyntaxException {
    ClassLoader classLoader = getClass().getClassLoader();
    URL resource = classLoader.getResource(this.filename);
    if (resource == null) {
      throw new IllegalArgumentException("File not found! " + this.filename);
    } else {
      return new File(resource.toURI());
    }
  }
}
