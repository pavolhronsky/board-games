package eu.tamarka.games.tictactoe.config;

public enum SubscriptionType {
  GAME,
  LIST;

  public static SubscriptionType getInstance(String type) {
    for (SubscriptionType t : values()) {
      if (t.toString().equals(type)) {
        return t;
      }
    }
    return null;
  }
}