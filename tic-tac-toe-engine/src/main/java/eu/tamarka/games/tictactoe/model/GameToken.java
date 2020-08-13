package eu.tamarka.games.tictactoe.model;

public enum GameToken {
  CIRCLE,
  CROSS;

  public static GameToken instanceOf(String token) {
    for (GameToken t : values()) {
      if (t.toString().equals(token)) {
        return t;
      }
    }
    return null;
  }
}