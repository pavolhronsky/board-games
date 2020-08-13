package eu.tamarka.games.tictactoe.model;

import eu.tamarka.games.tictactoe.model.move.GameModification;
import eu.tamarka.games.tictactoe.model.move.ModificationType;
import eu.tamarka.games.tictactoe.model.move.Move;
import java.util.ArrayList;
import java.util.List;

public class GameMove implements Move {

  private int row;

  private int column;

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

  @Override
  public boolean canPlay(GameState game) {
    return null == game.getFields()[3 * row + column].getToken();
  }

  @Override
  public List<GameModification> execute(GameState game) {
    List<GameModification> modifications = new ArrayList<>();
    GamePlayer player = game.getActivePlayerModel();
    GameField[] fields = game.getFields();
    fields[3 * row + column].setToken(player.getToken());
    fields[3 * row + column].setColor(player.getColor());
    String[] params = {row + "", column + "", player.getToken().toString(), player.getColor().toString()}; // row, column, token, color
    modifications.add(new GameModification(ModificationType.MOVE, 1, params));
    return modifications;
  }

  @Override
  public String toString() {
    return "{" +
        "\"row\":\"" + row + "\"" + ", " +
        "\"column\":\"" + column + "\"" +
        "}";
  }
}