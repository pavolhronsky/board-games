package eu.tamarka.authorization.config.oauth;

import java.util.Objects;

public class GooglePrincipal {

  private final String userId;

  private final String email;

  private final String name;

  private final String familyName;

  private final String givenName;

  private GooglePrincipal(Builder builder) {
    userId = builder.userId;
    email = builder.email;
    name = builder.name;
    familyName = builder.familyName;
    givenName = builder.givenName;
  }

  public String getUserId() {
    return userId;
  }

  public String getEmail() {
    return email;
  }

  public String getName() {
    return name;
  }

  public String getFamilyName() {
    return familyName;
  }

  public String getGivenName() {
    return givenName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GooglePrincipal principal = (GooglePrincipal) o;
    return userId.equals(principal.userId) &&
        email.equals(principal.email) &&
        name.equals(principal.name) &&
        familyName.equals(principal.familyName) &&
        givenName.equals(principal.givenName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, email, name, familyName, givenName);
  }

  @Override
  public String toString() {
    return "{"
        + "\"userId\":\"" + userId + "\""
        + ",\"email\":\"" + email + "\""
        + ",\"name\":\"" + name + "\""
        + ",\"familyName\":\"" + familyName + "\""
        + ",\"givenName\":\"" + givenName + "\""
        + "}";
  }

  public static final class Builder {

    private String userId;
    private String email;
    private String name;
    private String familyName;
    private String givenName;

    public Builder() {
    }

    public Builder withUserId(String userId) {
      this.userId = userId;
      return this;
    }

    public Builder withEmail(String email) {
      this.email = email;
      return this;
    }

    public Builder withName(String name) {
      this.name = name;
      return this;
    }

    public Builder withFamilyName(String familyName) {
      this.familyName = familyName;
      return this;
    }

    public Builder withGivenName(String givenName) {
      this.givenName = givenName;
      return this;
    }

    public GooglePrincipal build() {
      return new GooglePrincipal(this);
    }
  }
}