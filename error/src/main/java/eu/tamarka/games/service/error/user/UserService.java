package eu.tamarka.games.service.error.user;

import eu.tamarka.games.service.error.jpa.JpaService;
import eu.tamarka.games.service.error.rabbitmq.users.AuthorizationUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService extends JpaService<User, String, UserRepository> {

  private final Logger log = LogManager.getLogger(this.getClass());

  @Autowired
  public UserService(UserRepository repository) {
    super(repository);
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
    user = save(user);
    log.debug("Updated user: {}.", user);
  }
}