package eu.tamarka.games.tictactoe.rabbitmq.game;

public class RabbitMQGameMessage {

  private String gameName;

  private String gameId;

  private String message;

  public String getGameName() {
    return gameName;
  }

  public void setGameName(String gameName) {
    this.gameName = gameName;
  }

  public String getGameId() {
    return gameId;
  }

  public void setGameId(String gameId) {
    this.gameId = gameId;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public String toString() {
    return "{" +
        "\"gameName\":" + (gameName == null ? "null" : "\"" + gameName + "\"") + ", " +
        "\"gameId\":" + (gameId == null ? "null" : "\"" + gameId + "\"") + ", " +
        "\"message\":" + (message == null ? "null" : "\"" + message + "\"") +
        "}";
  }
}