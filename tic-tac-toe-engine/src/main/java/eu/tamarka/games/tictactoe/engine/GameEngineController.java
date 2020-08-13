package eu.tamarka.games.tictactoe.engine;

import eu.tamarka.games.tictactoe.config.GameDescription;
import eu.tamarka.games.tictactoe.model.GameColor;
import eu.tamarka.games.tictactoe.model.GameMove;
import eu.tamarka.games.tictactoe.model.GameToken;
import java.security.Principal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@CrossOrigin
public class GameEngineController {

  private final Logger log = LogManager.getLogger(this.getClass());

  private GameStateService gameStateService;
  private GameDescription gameDescription;

  @Autowired
  public GameEngineController(GameStateService gameStateService, GameDescription gameDescription) {
    this.gameStateService = gameStateService;
    this.gameDescription = gameDescription;
  }

  @GetMapping(
      path = "/game-description",
      produces = {MediaType.APPLICATION_JSON_UTF8_VALUE}
  )
  @ResponseBody
  public GameDescription getGameDescription() {
    return gameDescription;
  }

  @MessageMapping("/create")
  public void createGame(@Payload String gameName, Principal principal) {
    log.debug("Create game request: requester={}, gameName={}.", principal, gameName);
    gameStateService.createGame(gameName, principal);
  }

  @MessageMapping("/join/{gameId}")
  public void joinGame(@DestinationVariable String gameId, Principal principal) {
    log.debug("Join game request: requester={}, gameId={}.", principal, gameId);
    gameStateService.joinGame(gameId, principal);
  }

  @MessageMapping("/abandon/{gameId}")
  public void abandonGame(@DestinationVariable String gameId, Principal principal) {
    log.debug("Abandon game request: requester={}, gameId={}.", principal, gameId);
    gameStateService.abandonGame(gameId, principal);
  }

  @MessageMapping("/token/{gameId}")
  public void selectToken(@DestinationVariable String gameId, Principal principal, @Payload String token) {
    log.debug("Select token request: requester={}, new token={}, gameId={}.", principal, token, gameId);
    gameStateService.selectToken(gameId, principal, GameToken.instanceOf(token));
  }

  @MessageMapping("/color/{gameId}")
  public void selectColor(@DestinationVariable String gameId, Principal principal, @Payload String color) {
    log.debug("Select color request: requester={}, new token={}, gameId={}.", principal, color, gameId);
    gameStateService.selectColor(gameId, principal, GameColor.instanceOf(color));
  }

  @MessageMapping("/start/{gameId}")
  public void startGame(@DestinationVariable String gameId, Principal principal) {
    log.debug("Start game request: requester={}, gameId={}.", principal, gameId);
    gameStateService.startGame(gameId, principal);
  }

  @MessageMapping("/move/{gameId}")
  public void makeMove(@DestinationVariable String gameId, Principal principal, @Payload GameMove move) {
    log.debug("Make move request: requester={}, move={}, gameId={}.", principal, move, gameId);
    gameStateService.makeMove(gameId, principal, move);
  }
}