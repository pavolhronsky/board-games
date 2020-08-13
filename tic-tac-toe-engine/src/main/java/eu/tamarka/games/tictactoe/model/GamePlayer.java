package eu.tamarka.games.tictactoe.model;

import com.fasterxml.jackson.annotation.JsonView;
import eu.tamarka.games.tictactoe.model.view.View;
import eu.tamarka.games.tictactoe.user.User;

public class GamePlayer {

  @JsonView({View.GameList.class, View.PreGame.class, View.PublicGame.class})
  private User user;

  @JsonView({View.PreGame.class, View.PublicGame.class})
  private GameToken token;

  @JsonView({View.PreGame.class, View.PublicGame.class})
  private GameColor color;

  public GamePlayer() {
    //
  }

  public GamePlayer(User user) {
    this.user = user;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public GameToken getToken() {
    return token;
  }

  public void setToken(GameToken token) {
    this.token = token;
  }

  public GameColor getColor() {
    return color;
  }

  public void setColor(GameColor color) {
    this.color = color;
  }
}
