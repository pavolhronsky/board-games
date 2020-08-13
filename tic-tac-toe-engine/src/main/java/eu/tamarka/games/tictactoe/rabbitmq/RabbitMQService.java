package eu.tamarka.games.tictactoe.rabbitmq;

import eu.tamarka.games.tictactoe.rabbitmq.users.RabbitMQUserMessage;
import eu.tamarka.games.tictactoe.user.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQService {

  private final Logger log = LogManager.getLogger(this.getClass());

  private UserService userService;

  @Autowired
  public RabbitMQService(UserService userService) {
    this.userService = userService;
  }

  @RabbitListener(queues = "users.tictactoe")
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
}