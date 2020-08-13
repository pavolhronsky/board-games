package eu.tamarka.games.tictactoe.user;

import com.fasterxml.jackson.annotation.JsonView;
import eu.tamarka.games.tictactoe.model.view.View;
import java.security.Principal;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class User implements Principal {

  @Id
  @JsonView({View.GameList.class, View.PreGame.class, View.PublicGame.class})
  private String id;

  @JsonView({View.GameList.class, View.PreGame.class, View.PublicGame.class})
  private String name;

  private boolean verified;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @Override
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
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user = (User) o;
    return Objects.equals(id, user.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "{" +
        "\"id\":" + (id == null ? "null" : "\"" + id + "\"") + ", " +
        "\"name\":" + (name == null ? "null" : "\"" + name + "\"") + ", " +
        "\"verified\":\"" + verified + "\"" +
        "}";
  }
}