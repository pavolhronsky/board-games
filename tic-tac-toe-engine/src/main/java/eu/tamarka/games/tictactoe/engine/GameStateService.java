package eu.tamarka.games.tictactoe.engine;

import static org.springframework.messaging.core.AbstractMessageSendingTemplate.CONVERSION_HINT_HEADER;

import eu.tamarka.games.tictactoe.external.ChatService;
import eu.tamarka.games.tictactoe.guard.GuardService;
import eu.tamarka.games.tictactoe.model.GameColor;
import eu.tamarka.games.tictactoe.model.GameMove;
import eu.tamarka.games.tictactoe.model.GamePlayer;
import eu.tamarka.games.tictactoe.model.GameStage;
import eu.tamarka.games.tictactoe.model.GameState;
import eu.tamarka.games.tictactoe.model.GameToken;
import eu.tamarka.games.tictactoe.model.move.GameModification;
import eu.tamarka.games.tictactoe.model.move.ModificationType;
import eu.tamarka.games.tictactoe.model.view.View;
import eu.tamarka.games.tictactoe.user.User;
import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
public class GameStateService {

  private static final String GAME_NAME = "tictactoe";

  public static final Map<String, Object> GAME_LIST_HEADER;
  public static final Map<String, Object> PRE_GAME_HEADER;
  public static final Map<String, Object> PUBLIC_GAME_HEADER;

  private final Logger log = LogManager.getLogger(this.getClass());

  private GameStateRepository repository;
  private SimpMessageSendingOperations template;
  private GuardService guardService;
  private TicTacToeRules ticTacToeRules;
  private ChatService chatService;

  static {
    GAME_LIST_HEADER = new HashMap<>();
    GAME_LIST_HEADER.put(CONVERSION_HINT_HEADER, View.GameList.class);

    PRE_GAME_HEADER = new HashMap<>();
    PRE_GAME_HEADER.put(CONVERSION_HINT_HEADER, View.PreGame.class);

    PUBLIC_GAME_HEADER = new HashMap<>();
    PUBLIC_GAME_HEADER.put(CONVERSION_HINT_HEADER, View.PublicGame.class);
  }

  @Autowired
  public GameStateService(GameStateRepository repository, SimpMessageSendingOperations template, GuardService guardService,
      TicTacToeRules ticTacToeRules, ChatService chatService) {
    this.repository = repository;
    this.template = template;
    this.guardService = guardService;
    this.ticTacToeRules = ticTacToeRules;
    this.chatService = chatService;
  }

  public GameState findById(String id) {
    return repository.findById(id).orElseThrow(EntityNotFoundException::new);
  }

  public List<GameState> findAll() {
    return StreamSupport.stream(repository.findAll().spliterator(), false).collect(Collectors.toList());
  }

  public void createGame(String gameName, Principal principal) {
    User user = (User) principal;

    if (guardService.canCreateGame(user)) {
      GameState game = new GameState(gameName);
      List<GamePlayer> players = game.getPlayers();
      players.add(new GamePlayer(user));
      game.setPlayers(players);
      log.debug("Created new game: {}. Persisting.", game);
      repository.save(game);
      List<GameState> games = findAll();
      log.debug("Game persisted. Updating game list: {}.", games);
      template.convertAndSend("/tictactoe/games", games, GAME_LIST_HEADER);
      // Announce new game created
      template.convertAndSendToUser(principal.getName(), "tictactoe/created", game.getId());
    }
  }

  public void joinGame(String id, Principal principal) {
    GameState state = findById(id);
    log.debug("Game found: {}.", state);
    User user = (User) principal;

    if (guardService.canJoinGame(user, state)) {
      List<GamePlayer> players = state.getPlayers();
      players.add(new GamePlayer(user));
      state.setPlayers(players);
      repository.save(state);
      // Update lounge game list
      List<GameState> games = findAll();
      log.debug("Game persisted. Updating game list: {}.", games);
      template.convertAndSend("/tictactoe/games", games, GAME_LIST_HEADER);
      // Update particular game
      log.debug("Broadcasting updated pre-game: {}.", state);
      template.convertAndSend("/tictactoe/games/" + id, state, PRE_GAME_HEADER);
    }
  }

  public void abandonGame(String id, Principal principal) {
    GameState state = findById(id);
    log.debug("Game found: {}.", state);
    User user = (User) principal;

    if (guardService.canAbandonGame(user, state)) {
      if (guardService.canAbandonGame(user, state)) {
        List<GamePlayer> reducedPlayers = state.getPlayers().stream()
            .filter(player -> !player.getUser().equals(user))
            .collect(Collectors.toList());
        state.setPlayers(reducedPlayers);
        repository.save(state);
        // Update lounge game list
        List<GameState> games = findAll();
        log.debug("Game persisted. Updating game list: {}.", games);
        template.convertAndSend("/tictactoe/games", games, GAME_LIST_HEADER);
        // Update particular game
        log.debug("Broadcasting updated pre-game: {}.", state);
        template.convertAndSend("/tictactoe/games/" + id, state, PRE_GAME_HEADER);
      }
    }
  }

  public void selectToken(String id, Principal principal, GameToken token) {
    GameState state = findById(id);
    log.debug("Game found: {}.", state);
    User user = (User) principal;

    if (guardService.canSelectToken(user, state, token)) {
      state.getPlayers().stream()
          .filter(p -> p.getUser().equals(user))
          .findFirst()
          .get()
          .setToken(token);
      state = repository.save(state);
      // Update particular game
      log.debug("Broadcasting updated pre-game: {}.", state);
      template.convertAndSend("/tictactoe/games/" + id, state, PRE_GAME_HEADER);
    }
  }

  public void selectColor(String id, Principal principal, GameColor color) {
    GameState state = findById(id);
    log.debug("Game found: {}.", state);
    User user = (User) principal;

    if (guardService.canSelectColor(user, state, color)) {
      state.getPlayers().stream()
          .filter(p -> p.getUser().equals(user))
          .findFirst()
          .get()
          .setColor(color);
      state = repository.save(state);
      // Update particular game
      log.debug("Broadcasting updated pre-game: {}.", state);
      template.convertAndSend("/tictactoe/games/" + id, state, PRE_GAME_HEADER);
    }
  }

  public void startGame(String id, Principal principal) {
    GameState state = findById(id);
    log.debug("Game found: {}.", state);
    User user = (User) principal;

    if (guardService.canStartGame(user, state)) {
      List<GamePlayer> players = state.getPlayers();
      Collections.shuffle(players);
      state.setPlayers(players);
      state.setStage(GameStage.IN_PROGRESS);
      repository.save(state);
      // Update lounge game list
      List<GameState> games = findAll();
      log.debug("Game persisted. Updating game list: {}.", games);
      template.convertAndSend("/tictactoe/games", games, GAME_LIST_HEADER);
      // Update game's public part
      log.debug("Broadcasting updated game's public part: {}.", state);
      template.convertAndSend("/tictactoe/games/" + id, state, PUBLIC_GAME_HEADER);
    }
  }

  public void makeMove(String id, Principal principal, GameMove move) {
    GameState state = findById(id);
    log.debug("Game found: {}.", state);
    User user = (User) principal;

    if (guardService.canMakeMove(user, state, move)) {
      List<GameModification> modifications = move.execute(state);
      final String gameMessage = String.format("%s put %s on [%d,%d].", state.getActivePlayerModel().getUser().getName(),
          state.getActivePlayerModel().getToken(), move.getRow(), move.getColumn());
      chatService.sentMessage(GAME_NAME, state.getId(), gameMessage);

      if (ticTacToeRules.isOver(state)) {
        state.setStage(GameStage.FINISHED);
        String[] params = {GameStage.FINISHED.toString()};
        modifications.add(new GameModification(ModificationType.GAME_STAGE, 1, params));
        chatService.sentMessage(GAME_NAME, state.getId(), "Game is a tie");
      }

      if (ticTacToeRules.isWin(state)) {
        state.setStage(GameStage.FINISHED);
        String[] stageParams = {GameStage.FINISHED.toString()};
        modifications.add(new GameModification(ModificationType.GAME_STAGE, 1, stageParams));
        state.setWinner(state.getActivePlayerModel());
        String[] winnerParams = {state.getActivePlayer().toString()};
        modifications.add(new GameModification(ModificationType.WINNER, 1, winnerParams));
        chatService.sentMessage(GAME_NAME, state.getId(), "Winner: " + state.getWinner().getUser().getName());
      }

      nextActivePlayer(state);
      String[] params = {state.getActivePlayer().toString()};
      modifications.add(new GameModification(ModificationType.ACTIVE_PLAYER, 0, params));

      repository.save(state);
      // Update lounge game list
      List<GameState> games = findAll();
      log.debug("Game persisted. Updating game list: {}.", games);
      template.convertAndSend("/tictactoe/games", games, GAME_LIST_HEADER);
      // Send public modifications
      log.debug("Broadcasting public game updates: {}.", modifications);
      template.convertAndSend("/tictactoe/games/" + id + "/modifications", modifications);
    }
  }

  private void nextActivePlayer(GameState state) {
    int activePlayer = state.getActivePlayer();
    activePlayer = (activePlayer + 1) % state.getPlayers().size();
    state.setActivePlayer(activePlayer);
  }

  public void save(List<GameState> affectedGameStates) {
    repository.saveAll(affectedGameStates);
  }
}