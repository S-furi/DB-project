package db_project.utils;

import java.util.List;

import db_project.utils.authentication.AuthResponses;

public class Authenticator {

  public static AuthResponses authenticate(String email, String password) {

    final List<Credentials> credentials =
        new AbstractJsonReader<Credentials>() {}.setFileName("DbAuth.json")
            .retreiveData(Credentials.class);
    if (credentials.stream()
        .filter(t -> t.getEmail().equals(email))
        .filter(t -> t.getPassword().equals(password))
        .findAny()
        .isEmpty()) {
      return AuthResponses.DENIED;
    }

    final var privileges = credentials.stream().findFirst().get().getPrivileges();
    if (AuthResponses.ROOT.equals(privileges)) {
      return AuthResponses.ROOT;
    } else if (AuthResponses.USER.equals(privileges)) {
      return AuthResponses.USER;
    } else {
      return AuthResponses.DENIED;
    }
  }

  public class Credentials {

    private final String email;
    private final String password;
    private final String privileges;

    public Credentials(final String email, final String password, final String privileges) {
      this.email = email;
      this.password = password;
      this.privileges = privileges;
    }

    public String getEmail() {
      return this.email;
    }

    public String getPassword() {
      return this.password;
    }

    public String getPrivileges() {
      return this.privileges;
    }

    // public boolean equals(final Credentials credentials) {
    // }
  }
}
