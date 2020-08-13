package eu.tamarka.games.service.chat.user;

import java.security.Principal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class User implements Principal {

  @Id
  @Column(unique = true, nullable = false, length = 100)
  private String id;

  @Column(unique = true)
  private String name;

  private boolean verified;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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
        "\"id\": \"" + id + "\"," +
        "\"name\": \"" + name + "\"" +
        "\"verified\": \"" + verified + "\"" +
        "}";
  }
}