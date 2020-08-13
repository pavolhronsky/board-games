package eu.tamarka.games.service.error.rabbitmq.error;

import eu.tamarka.games.service.error.rabbitmq.users.AuthorizationUser;

public class RabbitMQErrorMessage {

  private AuthorizationUser user;

  private String content;

  public AuthorizationUser getUser() {
    return user;
  }

  public void setUser(AuthorizationUser user) {
    this.user = user;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  @Override
  public String toString() {
    return "{" +
        "\"user\":" + (user == null ? "null" : user) + ", " +
        "\"content\":" + (content == null ? "null" : "\"" + content + "\"") +
        "}";
  }
}