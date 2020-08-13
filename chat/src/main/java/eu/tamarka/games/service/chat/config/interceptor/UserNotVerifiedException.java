package eu.tamarka.games.service.chat.config.interceptor;

public class UserNotVerifiedException extends Exception {

  public UserNotVerifiedException(String message) {
    super(message);
  }
}
