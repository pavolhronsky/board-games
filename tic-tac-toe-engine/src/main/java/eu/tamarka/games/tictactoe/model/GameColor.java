package eu.tamarka.games.tictactoe.model;

public enum GameColor {
  CRIMSON,
  ROYALBLUE;

  public static GameColor instanceOf(String color) {
    for (GameColor c : values()) {
      if (c.toString().equals(color)) {
        return c;
      }
    }
    return null;
  }
}