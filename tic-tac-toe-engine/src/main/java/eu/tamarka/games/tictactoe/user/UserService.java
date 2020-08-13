package eu.tamarka.games.tictactoe.user;

import eu.tamarka.games.tictactoe.engine.GameStateService;
import eu.tamarka.games.tictactoe.model.GamePlayer;
import eu.tamarka.games.tictactoe.model.GameState;
import eu.tamarka.games.tictactoe.rabbitmq.users.AuthorizationUser;
import eu.tamarka.games.tictactoe.service.JpaService;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService extends JpaService<User, String, UserRepository> {

  private final Logger log = LogManager.getLogger(this.getClass());

  private GameStateService gameStateService;

  public UserService(UserRepository repository) {
    super(repository);
  }

  @Autowired
  public void setGameStateService(GameStateService gameStateService) {
    this.gameStateService = gameStateService;
  }

  public void delete(AuthorizationUser authUser) {
    User user = findById(authUser.getId());
    log.debug("User found: {}. Deleting.", user);
    delete(user);
    log.debug("User deleted");
  }

  public void create(AuthorizationUser authUser) {
    log.debug("Creating new user.");
    User user = new User();
    user.setId(authUser.getId());
    user.setName(authUser.getNickname());
    user.setVerified(authUser.isVerified());
    user = save(user);
    log.debug("Saved user: {}.", user);
  }

  public void update(AuthorizationUser authUser) {
    User user = findById(authUser.getId());
    log.debug("User found: {}. Updating.", user);
    user.setName(authUser.getNickname());
    user.setVerified(authUser.isVerified());
    User savedUser = save(user);
    // update games
    List<GameState> affectedGameStates = gameStateService.findAll().stream()
        .filter(gameState -> gameState.getPlayers().stream()
            .map(GamePlayer::getUser)
            .collect(Collectors.toList())
            .contains(savedUser))
        .collect(Collectors.toList());

    affectedGameStates.forEach(gameState -> {
      gameState.getPlayers().stream().filter(gamePlayer -> gamePlayer.getUser().equals(savedUser))
          .forEach(gamePlayer -> gamePlayer.setUser(savedUser));
    });
    gameStateService.save(affectedGameStates);
    log.debug("Updated user: {}.", user);
  }
}
