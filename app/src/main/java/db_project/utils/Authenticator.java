package db_project.utils;

import java.util.List;

public class Authenticator {

  public static boolean authenticate(String email, String password) {

    final List<Credentials> credentials =
        new AbstractJsonReader<Credentials>() {}.setFileName("DbAuth.json")
            .retreiveData(Credentials.class);
    return credentials.stream()
        .filter(t -> t.getEmail().equals(email))
        .filter(t -> t.getPassword().equals(password))
        .findAny()
        .isPresent();
  }

  public class Credentials {

    private final String email;
    private final String password;

    public Credentials(String email, String password) {
      this.email = email;
      this.password = password;
    }

    public String getEmail() {
      return this.email;
    }

    public String getPassword() {
      return this.password;
    }
  }
}
