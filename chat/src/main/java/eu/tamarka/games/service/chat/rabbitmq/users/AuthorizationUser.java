package eu.tamarka.games.service.chat.rabbitmq.users;

public class AuthorizationUser {

  private String id;

  private String email;

  private String givenName;

  private String familyName;

  private String nickname;

  private boolean verified;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getGivenName() {
    return givenName;
  }

  public void setGivenName(String givenName) {
    this.givenName = givenName;
  }

  public String getFamilyName() {
    return familyName;
  }

  public void setFamilyName(String familyName) {
    this.familyName = familyName;
  }

  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  public boolean isVerified() {
    return verified;
  }

  public void setVerified(boolean verified) {
    this.verified = verified;
  }

  @Override
  public String toString() {
    return "{" +
        "\"id\":" + (id == null ? "null" : "\"" + id + "\"") + ", " +
        "\"email\":" + (email == null ? "null" : "\"" + email + "\"") + ", " +
        "\"givenName\":" + (givenName == null ? "null" : "\"" + givenName + "\"") + ", " +
        "\"familyName\":" + (familyName == null ? "null" : "\"" + familyName + "\"") + ", " +
        "\"nickname\":" + (nickname == null ? "null" : "\"" + nickname + "\"") + ", " +
        "\"verified\":\"" + verified + "\"" +
        "}";
  }
}