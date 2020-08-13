package eu.tamarka.games.tictactoe.guard;

import eu.tamarka.games.tictactoe.config.GameDescription;
import eu.tamarka.games.tictactoe.engine.exception.ColorAlreadyTakenException;
import eu.tamarka.games.tictactoe.engine.exception.GameAlreadyRunningOrFinishedException;
import eu.tamarka.games.tictactoe.engine.exception.GameNotInProgressException;
import eu.tamarka.games.tictactoe.engine.exception.InvalidMoveException;
import eu.tamarka.games.tictactoe.engine.exception.PlayerNumberNotWithinBoundsException;
import eu.tamarka.games.tictactoe.engine.exception.SomebodyIsMissingColorException;
import eu.tamarka.games.tictactoe.engine.exception.SomebodyIsMissingTokenException;
import eu.tamarka.games.tictactoe.engine.exception.TokenAlreadyTakenException;
import eu.tamarka.games.tictactoe.engine.exception.UserAlreadyInGameException;
import eu.tamarka.games.tictactoe.engine.exception.UserNotAuthenticatedException;
import eu.tamarka.games.tictactoe.engine.exception.UserNotInGameException;
import eu.tamarka.games.tictactoe.engine.exception.UserNotOnTurnException;
import eu.tamarka.games.tictactoe.model.GameColor;
import eu.tamarka.games.tictactoe.model.GameMove;
import eu.tamarka.games.tictactoe.model.GamePlayer;
import eu.tamarka.games.tictactoe.model.GameStage;
import eu.tamarka.games.tictactoe.model.GameState;
import eu.tamarka.games.tictactoe.model.GameToken;
import eu.tamarka.games.tictactoe.service.ErrorService;
import eu.tamarka.games.tictactoe.user.User;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GuardService {

  private final Logger log = LogManager.getLogger(this.getClass());

  private ErrorService errorService;
  private GameDescription gameDescription;

  @Autowired
  public GuardService(ErrorService errorService, GameDescription gameDescription) {
    this.errorService = errorService;
    this.gameDescription = gameDescription;
  }

  public boolean canCreateGame(User user) {
    try {
      checkIfUserIsAuthenticated(user);
    } catch (UserNotAuthenticatedException e) {
      log.info("Unauthorized user attempted to create a game.");
      return false;
    }
    return true;
  }

  private void checkIfUserIsAuthenticated(User user) throws UserNotAuthenticatedException {
    log.debug("Authenticated user={}.", user);
    if (user.getId() == null || user.getName() == null) {
      throw new UserNotAuthenticatedException();
    }
  }

  public boolean canJoinGame(User user, GameState state) {
    try {
      checkIfUserIsAuthenticated(user);
      checkIfUserIsNotInGame(user, state);
    } catch (UserNotAuthenticatedException e) {
      log.info("Unauthorized user attempted to join the game.");
      return false;
    } catch (UserAlreadyInGameException e) {
      errorService.send(user, String.format("You are already in GAME_ID=%s.", state.getId()));
      return false;
    }
    return true;
  }

  private void checkIfUserIsNotInGame(User user, GameState state) throws UserAlreadyInGameException {
    if (state.getPlayers().stream().anyMatch(p -> p.getUser().equals(user))) {
      throw new UserAlreadyInGameException();
    }
  }

  public boolean canAbandonGame(User user, GameState state) {
    try {
      checkIfUserIsAuthenticated(user);
      checkIfUserIsInGame(user, state);
      checkIfGameIsInCreatedStage(state);
    } catch (UserNotAuthenticatedException e) {
      log.info("Unauthorized user attempted to abandon the game.");
      return false;
    } catch (UserNotInGameException e) {
      errorService.send(user, String.format("You are not a member of GAME_ID=%s.", state.getId()));
      return false;
    } catch (GameAlreadyRunningOrFinishedException e) {
      errorService.send(user, String.format("Already started GAME_ID=%s.", state.getId()));
      return false;
    }
    return true;
  }

  private void checkIfUserIsInGame(User user, GameState state) throws UserNotInGameException {
    if (state.getPlayers().stream().noneMatch(p -> p.getUser().equals(user))) {
      throw new UserNotInGameException();
    }
  }

  private void checkIfGameIsInCreatedStage(GameState state) throws GameAlreadyRunningOrFinishedException {
    if (state.getStage() != GameStage.CREATED) {
      throw new GameAlreadyRunningOrFinishedException();
    }
  }

  public boolean canSelectColor(User user, GameState state, GameColor color) {
    try {
      checkIfUserIsAuthenticated(user);
      checkIfUserIsInGame(user, state);
      checkIfColorIsTaken(state.getPlayers(), color);
    } catch (UserNotAuthenticatedException e) {
      log.info("Unauthorized user attempted to select a color.");
      return false;
    } catch (UserNotInGameException e) {
      errorService.send(user, String.format("You are not a member of GAME_ID=%s.", state.getId()));
      return false;
    } catch (ColorAlreadyTakenException e) {
      errorService.send(user, String.format("COLOR=%s is already taken.", color));
      return false;
    }
    return true;
  }

  private void checkIfColorIsTaken(List<GamePlayer> players, GameColor color) throws ColorAlreadyTakenException {
    if (players.stream().map(GamePlayer::getColor).anyMatch(c -> c == color)) {
      throw new ColorAlreadyTakenException();
    }
  }

  public boolean canSelectToken(User user, GameState state, GameToken token) {
    try {
      checkIfUserIsAuthenticated(user);
      checkIfUserIsInGame(user, state);
      checkIfTokenIsTaken(state.getPlayers(), token);
    } catch (UserNotAuthenticatedException e) {
      log.info("Unauthorized user attempted to select a token.");
      return false;
    } catch (UserNotInGameException e) {
      errorService.send(user, String.format("You are not a member of GAME_ID=%s.", state.getId()));
      return false;
    } catch (TokenAlreadyTakenException e) {
      errorService.send(user, String.format("TOKEN=%s is already taken.", token));
      return false;
    }
    return true;
  }

  private void checkIfTokenIsTaken(List<GamePlayer> players, GameToken token) throws TokenAlreadyTakenException {
    if (players.stream().map(GamePlayer::getToken).anyMatch(t -> t == token)) {
      throw new TokenAlreadyTakenException();
    }
  }

  public boolean canStartGame(User user, GameState state) {
    try {
      checkIfUserIsAuthenticated(user);
      checkIfUserIsInGame(user, state);
      checkIfGameIsInCreatedStage(state);
      checkIfNumberOfPlayersIsWithinMinMaxRange(state.getPlayers(), gameDescription.getMinNumberOfPlayers(), gameDescription.getMaxNumberOfPlayers());
      checkIfEverybodyHasChosenColor(state.getPlayers());
      checkIfEverybodyHasChosenToken(state.getPlayers());
    } catch (UserNotAuthenticatedException e) {
      log.info("Unauthorized user attempted to start the game.");
      return false;
    } catch (UserNotInGameException e) {
      errorService.send(user, String.format("You are not a member of GAME_ID=%s.", state.getId()));
      return false;
    } catch (GameAlreadyRunningOrFinishedException e) {
      errorService.send(user, String.format("GAME_ID=%s is already in progress or finished.", state.getId()));
      return false;
    } catch (PlayerNumberNotWithinBoundsException e) {
      errorService.send(user, String.format("Player number limit has to be within [%d,%d]. Actual size: %d.", gameDescription.getMinNumberOfPlayers(),
          gameDescription.getMaxNumberOfPlayers(), state.getPlayers().size()));
      return false;
    } catch (SomebodyIsMissingTokenException e) {
      errorService.send(user, "Not everybody has chosen his/hers token.");
      return false;
    } catch (SomebodyIsMissingColorException e) {
      errorService.send(user, "Not everybody has chosen his/hers color.");
      return false;
    }
    return true;
  }

  private void checkIfNumberOfPlayersIsWithinMinMaxRange(List<GamePlayer> players, int min, int max) throws PlayerNumberNotWithinBoundsException {
    log.debug("Min size={}, max size={}, actual size={}.", min, max, players.size());
    if (!(players.size() >= min && players.size() <= max)) {
      throw new PlayerNumberNotWithinBoundsException();
    }
  }

  private void checkIfEverybodyHasChosenColor(List<GamePlayer> players) throws SomebodyIsMissingColorException {
    if (players.stream().anyMatch(p -> p.getColor() == null)) {
      throw new SomebodyIsMissingColorException();
    }
  }

  private void checkIfEverybodyHasChosenToken(List<GamePlayer> players) throws SomebodyIsMissingTokenException {
    if (players.stream().anyMatch(p -> p.getToken() == null)) {
      throw new SomebodyIsMissingTokenException();
    }
  }

  public boolean canMakeMove(User user, GameState state, GameMove move) {
    try {
      checkIfUserIsAuthenticated(user);
      checkIfUserIsInGame(user, state);
      checkIfUserIsActivePlayer(user, state);
      checkIfMoveIsValid(state, move);
      checkIfGameIsStillRunning(state);
    } catch (UserNotAuthenticatedException e) {
      log.info("Unauthorized user attempted to make a move.");
      return false;
    } catch (UserNotInGameException e) {
      errorService.send(user, String.format("You are not a member of GAME_ID=%s.", state.getId()));
      return false;
    } catch (InvalidMoveException e) {
      errorService.send(user, String.format("Field [%d,%d] is already occupied or does not exist.", move.getRow(), move.getColumn()));
      return false;
    } catch (UserNotOnTurnException e) {
      errorService.send(user, "It is not your turn.");
      return false;
    } catch (GameNotInProgressException e) {
      errorService.send(user, String.format("GAME_ID=%s has not started yet.", state.getId()));
      return false;
    }
    return true;
  }

  private void checkIfUserIsActivePlayer(User user, GameState state) throws UserNotOnTurnException {
    if (!state.getActivePlayerModel().getUser().equals(user)) {
      throw new UserNotOnTurnException();
    }
  }

  private void checkIfMoveIsValid(GameState state, GameMove move) throws InvalidMoveException {
    if (!move.canPlay(state)) {
      throw new InvalidMoveException();
    }
  }

  private void checkIfGameIsStillRunning(GameState state) throws GameNotInProgressException {
    if (state.getStage() != GameStage.IN_PROGRESS) {
      throw new GameNotInProgressException();
    }
  }
}