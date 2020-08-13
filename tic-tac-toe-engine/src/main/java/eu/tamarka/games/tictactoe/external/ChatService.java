package eu.tamarka.games.tictactoe.external;

import eu.tamarka.games.tictactoe.rabbitmq.game.RabbitMQGameMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

  private RabbitTemplate template;

  @Autowired
  public void setTemplate(RabbitTemplate template) {
    this.template = template;
  }

  public void sentMessage(String gameName, String gameId, String message) {
    RabbitMQGameMessage gameMessage = new RabbitMQGameMessage();
    gameMessage.setGameName(gameName);
    gameMessage.setGameId(gameId);
    gameMessage.setMessage(message);
    template.convertAndSend("game-messages.fanout", "", gameMessage);
  }
}