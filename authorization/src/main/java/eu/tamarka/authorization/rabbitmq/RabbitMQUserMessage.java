package eu.tamarka.authorization.rabbitmq;

import eu.tamarka.authorization.user.User;

public class RabbitMQUserMessage {

  private UserMessageType type;

  private User user;

  public UserMessageType getType() {
    return type;
  }

  public void setType(UserMessageType type) {
    this.type = type;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  @Override
  public String toString() {
    return "{" +
        "\"type\":" + (type == null ? "null" : type) + ", " +
        "\"user\":" + (user == null ? "null" : user) +
        "}";
  }
}