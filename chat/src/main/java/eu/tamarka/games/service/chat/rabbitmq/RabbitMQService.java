package eu.tamarka.games.service.chat.rabbitmq;

import eu.tamarka.games.service.chat.message.ChatMessage;
import eu.tamarka.games.service.chat.message.ChatMessageService;
import eu.tamarka.games.service.chat.message.MessageType;
import eu.tamarka.games.service.chat.rabbitmq.game.RabbitMQGameMessage;
import eu.tamarka.games.service.chat.rabbitmq.users.RabbitMQUserMessage;
import eu.tamarka.games.service.chat.user.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQService {

  private final Logger log = LogManager.getLogger(this.getClass());

  private UserService userService;

  private ChatMessageService chatMessageService;

  private SimpMessagingTemplate template;

  @Autowired
  public RabbitMQService(UserService userService, ChatMessageService chatMessageService) {
    this.userService = userService;
    this.chatMessageService = chatMessageService;
  }

  @Autowired
  public void setTemplate(SimpMessagingTemplate template) {
    this.template = template;
  }

  @RabbitListener(queues = "users.chat")
  public void onUsersMessage(RabbitMQUserMessage message) {
    log.debug("User message from RabbitMQ: {}.", message);
    switch (message.getType()) {
      case DELETE:
        userService.delete(message.getUser());
        break;
      case NEW:
        userService.create(message.getUser());
        break;
      case UPDATE:
        userService.update(message.getUser());
    }
  }

  @RabbitListener(queues = "game-messages.chat")
  public void onGameMessage(RabbitMQGameMessage message) {
    log.debug("New game message: {}.", message);
    ChatMessage chatMessage = new ChatMessage();
    chatMessage.setGameName(message.getGameName());
    chatMessage.setGameId(message.getGameId());
    chatMessage.setContent(message.getMessage());
    chatMessage.setType(MessageType.GAME);
    ChatMessage savedMessage = chatMessageService.save(chatMessage);
    log.debug("Saved chat message: {}.", savedMessage);
    String destination = "/chat/games/" + message.getGameName() + "/" + message.getGameId();
    template.convertAndSend(destination, savedMessage);
    log.info("Message: {} broadcasted to: {}.", savedMessage, destination);
  }
}