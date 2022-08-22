package db_project.utils.authentication;

public enum AuthResponses {
  USER("user"),
  ROOT("root"),
  DENIED("denied");

  private final String strFormat;
  private AuthResponses(final String value) {
    this.strFormat = value;
  }

  public String getValue() {
    return this.strFormat;
  }

  public boolean equals(final String privileges) {
    return this.strFormat.equals(privileges);
  }
}
