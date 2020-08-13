package eu.tamarka.games.service.chat.config.interceptor;

import eu.tamarka.games.service.chat.message.ChatMessage;
import eu.tamarka.games.service.chat.message.ChatMessageService;
import eu.tamarka.games.service.chat.message.MessageType;
import java.security.Principal;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

@Component
public class WebSocketListener {

  private static final int GAME_ID_LENGTH = 10;

  private final Logger log = LogManager.getLogger(this.getClass());

  private SimpMessageSendingOperations template;
  private ChatMessageService chatMessageService;

  @Autowired
  public WebSocketListener(SimpMessageSendingOperations template, ChatMessageService chatMessageService) {
    this.template = template;
    this.chatMessageService = chatMessageService;
  }

  @EventListener
  public void handleWebSocketSubscribeListener(SessionSubscribeEvent event) {
    StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

    if (accessor.getCommand() == StompCommand.SUBSCRIBE) {
      log.info("Subscribe event triggered.");
      String destination = accessor.getDestination();
      String gameId = destination.substring(destination.length() - GAME_ID_LENGTH);
      String destinationWithoutGameId = destination.substring(0, destination.length() - GAME_ID_LENGTH - 1);
      String gameName = destinationWithoutGameId.substring(destinationWithoutGameId.lastIndexOf("/") + 1);
      Principal user = accessor.getUser();
      // load history for user
      List<ChatMessage> history = chatMessageService.findAllByGameNameAndGameId(gameName, gameId);
      template.convertAndSendToUser(user.getName(), destination, history);
      log.info("History loaded and sent.");
      // announce joined
      String messageContent = user.getName() + " joined!";
      ChatMessage msg = createConnectionMessage(messageContent);
      template.convertAndSend(destination, msg);
      log.info("Joined message sent.");
    }
  }

  @EventListener
  public void handleWebSocketUnsubscribeListener(SessionUnsubscribeEvent event) {
    StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

    if (accessor.getCommand() == StompCommand.UNSUBSCRIBE) {
      log.info("Unsubscribe event triggered.");
      // announce left
      String messageContent = accessor.getUser().getName() + " left!";
      ChatMessage msg = createConnectionMessage(messageContent);
      template.convertAndSend(accessor.getDestination(), msg);
      log.info("Left message sent.");
    }
  }

  private ChatMessage createConnectionMessage(String messageContent) {
    ChatMessage message = new ChatMessage();
    message.setContent(messageContent);
    message.setType(MessageType.CONNECTION);
    return message;
  }
}
