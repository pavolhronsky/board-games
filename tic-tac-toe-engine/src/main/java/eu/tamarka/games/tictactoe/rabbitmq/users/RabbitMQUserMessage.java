package eu.tamarka.games.tictactoe.rabbitmq.users;

public class RabbitMQUserMessage {

  private UserMessageType type;

  private AuthorizationUser user;

  public UserMessageType getType() {
    return type;
  }

  public void setType(UserMessageType type) {
    this.type = type;
  }

  public AuthorizationUser getUser() {
    return user;
  }

  public void setUser(AuthorizationUser user) {
    this.user = user;
  }

  @Override
  public String toString() {
    return "{" +
        "\"type\":" + (type == null ? "null" : "\"" + type + "\"") + ", " +
        "\"user\":" + (user == null ? "null" : user) +
        "}";
  }
}