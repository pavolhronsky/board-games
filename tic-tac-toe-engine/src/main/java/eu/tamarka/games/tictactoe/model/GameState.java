package eu.tamarka.games.tictactoe.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import eu.tamarka.games.tictactoe.model.view.View;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("GameState")
public class GameState implements Serializable {

  @JsonView({View.GameList.class, View.PreGame.class, View.PublicGame.class})
  private String id;

  @JsonView({View.GameList.class, View.PreGame.class, View.PublicGame.class})
  private String name;

  @JsonView({View.PublicGame.class})
  private GameField[] fields;

  @JsonView({View.GameList.class, View.PreGame.class, View.PublicGame.class})
  private List<GamePlayer> players;

  @JsonView({View.GameList.class, View.PreGame.class, View.PublicGame.class})
  private GameStage stage;

  @JsonView({View.PublicGame.class})
  private Integer activePlayer;

  @JsonView({View.PublicGame.class})
  private GamePlayer winner;

  public GameState() {
    //
  }

  public GameState(String name) {
    this.name = name;
    id = RandomStringUtils.random(10, true, true);
    // initialize fields
    fields = new GameField[9];
    for (int i = 0; i < 9; i++) {
      fields[i] = new GameField(i / 3, i % 3);
    }
    // initialize players
    players = new ArrayList<>(2);
    // initialize stage
    stage = GameStage.CREATED;
    // initialize active player
    activePlayer = 0;
  }

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

  public GameField[] getFields() {
    return fields;
  }

  public void setFields(GameField[] fields) {
    this.fields = fields;
  }

  public List<GamePlayer> getPlayers() {
    return players;
  }

  public void setPlayers(List<GamePlayer> players) {
    this.players = players;
  }

  public GameStage getStage() {
    return stage;
  }

  public void setStage(GameStage stage) {
    this.stage = stage;
  }

  @JsonIgnore
  public GamePlayer getActivePlayerModel() {
    if (stage == GameStage.CREATED) {
      return null;
    }
    return players.get(activePlayer);
  }

  public Integer getActivePlayer() {
    return activePlayer;
  }

  public void setActivePlayer(Integer activePlayer) {
    this.activePlayer = activePlayer;
  }

  public GamePlayer getWinner() {
    return winner;
  }

  public void setWinner(GamePlayer winner) {
    this.winner = winner;
  }

  @Override
  public String toString() {
    return "{" +
        "\"id\":" + (id == null ? "null" : "\"" + id + "\"") + ", " +
        "\"name\":" + (name == null ? "null" : "\"" + name + "\"") + ", " +
        "\"fields\":" + Arrays.toString(fields) + ", " +
        "\"players\":" + (players == null ? "null" : Arrays.toString(players.toArray())) + ", " +
        "\"stage\":" + (stage == null ? "null" : stage) + ", " +
        "\"activePlayer\":" + (activePlayer == null ? "null" : "\"" + activePlayer + "\"") + ", " +
        "\"winner\":" + (winner == null ? "null" : winner) +
        "}";
  }
}