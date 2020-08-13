package eu.tamarka.games.tictactoe.model;

import com.fasterxml.jackson.annotation.JsonView;
import eu.tamarka.games.tictactoe.model.view.View;

public class GameField {

  @JsonView({View.PublicGame.class})
  private int row;

  @JsonView({View.PublicGame.class})
  private int column;

  @JsonView({View.PublicGame.class})
  private GameToken token;

  @JsonView({View.PublicGame.class})
  private GameColor color;

  public GameField() {
    //
  }

  public GameField(int row, int column) {
    this.row = row;
    this.column = column;
  }

  public int getRow() {
    return row;
  }

  public void setRow(int row) {
    this.row = row;
  }

  public int getColumn() {
    return column;
  }

  public void setColumn(int column) {
    this.column = column;
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

  @Override
  public String toString() {
    return "{" +
        "\"row\":\"" + row + "\"" + ", " +
        "\"column\":\"" + column + "\"" + ", " +
        "\"token\":" + (token == null ? "null" : token) + ", " +
        "\"color\":" + (color == null ? "null" : color) +
        "}";
  }
}