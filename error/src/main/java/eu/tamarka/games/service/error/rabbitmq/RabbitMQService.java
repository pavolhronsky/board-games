package eu.tamarka.games.service.error.rabbitmq;

import eu.tamarka.games.service.error.rabbitmq.error.RabbitMQErrorMessage;
import eu.tamarka.games.service.error.rabbitmq.users.RabbitMQUserMessage;
import eu.tamarka.games.service.error.user.UserService;
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

  private SimpMessagingTemplate template;

  @Autowired
  public RabbitMQService(UserService userService) {
    this.userService = userService;
  }

  @Autowired
  public void setTemplate(SimpMessagingTemplate template) {
    this.template = template;
  }

  @RabbitListener(queues = "users.error")
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

  @RabbitListener(queues = "general.error")
  public void onErrorMessage(RabbitMQErrorMessage message) {
    log.debug("Error message from RabbitMQ: {}.", message);
    template.convertAndSendToUser(message.getUser().getNickname(), "/error", message.getContent());
  }
}