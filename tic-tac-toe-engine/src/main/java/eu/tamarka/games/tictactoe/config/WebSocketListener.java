package eu.tamarka.games.tictactoe.config;

import eu.tamarka.games.tictactoe.engine.GameStateService;
import eu.tamarka.games.tictactoe.model.GameState;
import eu.tamarka.games.tictactoe.user.User;
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

@Component
public class WebSocketListener {

  private final Logger log = LogManager.getLogger(this.getClass());

  private final SimpMessageSendingOperations template;
  private final GameStateService gameStateService;

  @Autowired
  public WebSocketListener(SimpMessageSendingOperations template, GameStateService gameStateService) {
    this.template = template;
    this.gameStateService = gameStateService;
  }

  @EventListener
  public void handleWebSocketSubscribeListener(SessionSubscribeEvent event) {
    StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

    if (accessor.getCommand() == StompCommand.SUBSCRIBE) {
      String destination = accessor.getDestination();
      User user = (User) accessor.getUser();
      log.info("User: {}.", user.getName());
      if (null != accessor.getNativeHeader("type")) {
        SubscriptionType subType = SubscriptionType.getInstance(accessor.getNativeHeader("type").get(0));
        log.info("Subscribing with type: {}.", subType);
        switch (subType) {
          case GAME:
            String gameId = destination.substring(destination.lastIndexOf("/") + 1);
            GameState state = gameStateService.findById(gameId);
            template.convertAndSendToUser(user.getName(), destination, state);
            break;
          case LIST:
            List<GameState> games = gameStateService.findAll();
            log.info("Subscribing to list of games\n\tDestination: {}\n\tGames: {}", destination, games);
            template.convertAndSendToUser(user.getName(), destination, games, GameStateService.GAME_LIST_HEADER);
            break;
          default:
            //
        }
      }
    }
  }
}