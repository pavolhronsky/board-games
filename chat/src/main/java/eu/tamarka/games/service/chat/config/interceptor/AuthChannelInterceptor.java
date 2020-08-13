package eu.tamarka.games.service.chat.config.interceptor;

import eu.tamarka.games.service.chat.user.User;
import eu.tamarka.games.service.chat.user.UserService;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class AuthChannelInterceptor implements ChannelInterceptor {

  private static final String USER_ID_HEADER = "userId";

  private final Logger log = LogManager.getLogger(this.getClass());

  private UserService userService;

  @Autowired
  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

    if (StompCommand.CONNECT == accessor.getCommand()) {
      log.debug("Connecting to websocket.");
      try {
        User user = retrieveUserFromNativeHeaders(accessor);
        accessor.setUser(user);
      } catch (AuthenticationException e) {
        log.error("Authentication failed. Message: {}.", e.getMessage(), e);
        return null;
      } catch (UserNotVerifiedException e) {
        log.error("User is not yet verified. Message: {}.", e.getMessage(), e);
        return null;
      }
    }

    return message;
  }

  private User retrieveUserFromNativeHeaders(StompHeaderAccessor accessor) throws AuthenticationException, UserNotVerifiedException {
    List<String> nativeHeaders = accessor.getNativeHeader(USER_ID_HEADER);

    if (null == nativeHeaders || nativeHeaders.isEmpty()) {
      throw new AuthenticationException("Authentication token is missing.");
    }

    String userId = nativeHeaders.get(0);
    try {
      User user = userService.findById(userId);
      if (user.isVerified()) {
        return user;
      }
      throw new UserNotVerifiedException("User is not yet verified.");
    } catch (EntityNotFoundException e) {
      throw new AuthenticationException("User does not exist.");
    }
  }
}