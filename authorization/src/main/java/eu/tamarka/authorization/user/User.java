package eu.tamarka.authorization.user;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User implements Serializable {

  @Id
  @Column(unique = true, nullable = false, length = 100)
  private String id;

  @Column(unique = true, nullable = false, length = 100)
  private String email;

  @Column(nullable = false)
  private String givenName;

  @Column(nullable = false)
  private String familyName;

  private String nickname;

  private Boolean verified;

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

  public Boolean isVerified() {
    return verified;
  }

  public void setVerified(Boolean verified) {
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
        "\"verified\":" + (verified == null ? "null" : "\"" + verified + "\"") +
        "}";
  }
}