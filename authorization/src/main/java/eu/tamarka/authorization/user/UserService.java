package eu.tamarka.authorization.user;

import eu.tamarka.authorization.config.oauth.GooglePrincipal;
import eu.tamarka.authorization.jpa.JpaService;
import eu.tamarka.authorization.rabbitmq.RabbitMQUserMessage;
import eu.tamarka.authorization.rabbitmq.UserMessageType;
import javax.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService extends JpaService<User, String, UserRepository> {

  private final static String USERS_EXCHANGE = "users.fanout";

  private final Logger log = LogManager.getLogger(this.getClass());

  private RabbitTemplate template;

  @Autowired
  public UserService(UserRepository userRepository) {
    super(userRepository);
  }

  @Autowired
  public void setTemplate(RabbitTemplate template) {
    this.template = template;
  }

  public ResponseEntity<User> getCurrentUser() {
    GooglePrincipal principal = (GooglePrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    log.debug("Google principal: {}.", principal);
    try {
      User user = findById(principal.getUserId());
      log.debug("User found: {}. Returning.", user);
      return new ResponseEntity<>(user, HttpStatus.CREATED);
    } catch (EntityNotFoundException e) {
      log.debug("Creating new user.");
      User user = createNewUser(principal);
      log.debug("User created: {}. Persisting.", user);
      user = save(user);
      log.debug("User persisted: {}.", user);
      RabbitMQUserMessage message = new RabbitMQUserMessage();
      message.setType(UserMessageType.NEW);
      message.setUser(user);
      log.debug("Publishing: exchange={}, message={}.", USERS_EXCHANGE, message);
      try {
        template.convertAndSend(USERS_EXCHANGE, "", message);
        return new ResponseEntity<>(user, HttpStatus.OK);
      } catch (AmqpException ae) {
        log.warn("Publishing was not successful. Reverting user creation. Exception: {}.", ae.getMessage(), ae);
        delete(user);
        log.debug("User deleted.");
        return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
      }
    }
  }

  private User createNewUser(GooglePrincipal principal) {
    User user = new User();
    user.setId(principal.getUserId());
    user.setEmail(principal.getEmail());
    user.setGivenName(principal.getGivenName());
    user.setFamilyName(principal.getFamilyName());
    user.setNickname(principal.getGivenName());
    user.setVerified(false);
    return user;
  }

  public ResponseEntity<User> updateUser() {
    GooglePrincipal principal = (GooglePrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    log.debug("Google principal: {}.", principal);
    User user = findById(principal.getUserId());
    log.debug("User found: {}. Updating.", user);
    user.setGivenName(principal.getGivenName());
    user.setFamilyName(principal.getFamilyName());
    user = save(user);
    log.debug("Updated user: {}.", user);
    return new ResponseEntity<>(user, HttpStatus.OK);
  }

  public ResponseEntity<User> updateNickname(User userWithNewNickname) {
    GooglePrincipal principal = (GooglePrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    log.debug("Google principal: {}.", principal);
    User user = findById(principal.getUserId());
    log.debug("User found: {}.", user);
    User oldUser = new User();
    oldUser.setNickname(user.getNickname());
    log.debug("Updating existing user.");
    user.setNickname(userWithNewNickname.getNickname());
    user = save(user);
    log.debug("Updated user: {}.", user);

    RabbitMQUserMessage message = new RabbitMQUserMessage();
    message.setType(UserMessageType.UPDATE);
    message.setUser(user);
    log.debug("Publishing: exchange={}, message={}.", USERS_EXCHANGE, message);
    try {
      template.convertAndSend(USERS_EXCHANGE, "", message);
      return new ResponseEntity<>(user, HttpStatus.OK);
    } catch (AmqpException ae) {
      log.warn("Publishing was not successful. Reverting nickname locally. Exception: {}.", ae.getMessage(), ae);
      user.setNickname(oldUser.getNickname());
      user = save(user);
      return new ResponseEntity<>(user, HttpStatus.BAD_GATEWAY);
    }
  }

  public ResponseEntity<User> deleteCurrentUser() {
    GooglePrincipal principal = (GooglePrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    log.debug("Google principal: {}.", principal);
    User user = findById(principal.getUserId());
    log.debug("User found: {}. Deleting.", user);
    delete(user);
    log.debug("User deleted.");

    RabbitMQUserMessage message = new RabbitMQUserMessage();
    message.setType(UserMessageType.DELETE);
    message.setUser(user);
    log.debug("Publishing: exchange={}, message={}.", USERS_EXCHANGE, message);
    try {
      template.convertAndSend(USERS_EXCHANGE, "", message);
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (AmqpException ae) {
      log.warn("Publishing was not successful. Reverting user deletion. Exception: {}.", ae.getMessage(), ae);
      user = save(user);
      log.debug("User persisted: {}.", user);
      return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
    }
  }

  public ResponseEntity<Page<User>> findPage(Pageable pageable) {
    Page<User> page = super.findAll(pageable);
    log.debug("Returning page of users: {}", page.getContent());
    return new ResponseEntity<>(page, HttpStatus.OK);
  }

  public ResponseEntity<User> verifyUser(String id) {
    try {
      User user = findById(id);
      user.setVerified(true);
      log.debug("User found and updated: {}. Returning.", user);
      RabbitMQUserMessage message = new RabbitMQUserMessage();
      message.setType(UserMessageType.UPDATE);
      message.setUser(user);
      try {
        log.debug("Publishing: exchange={}, message={}.", USERS_EXCHANGE, message);
        template.convertAndSend(USERS_EXCHANGE, "", message);
        return new ResponseEntity<>(save(user), HttpStatus.OK);
      } catch (AmqpException e) {
        log.warn("Publishing was not successful. Reverting user verification status to false. Exception: {}.", e.getMessage(), e);
        user.setVerified(false);
        user = save(user);
        return new ResponseEntity<>(user, HttpStatus.BAD_GATEWAY);
      }
    } catch (EntityNotFoundException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  public ResponseEntity<User> disproveUser(String id) {
    try {
      User user = findById(id);
      user.setVerified(false);
      log.debug("User found and updated: {}. Returning.", user);
      RabbitMQUserMessage message = new RabbitMQUserMessage();
      message.setType(UserMessageType.UPDATE);
      message.setUser(user);
      try {
        log.debug("Publishing: exchange={}, message={}.", USERS_EXCHANGE, message);
        template.convertAndSend(USERS_EXCHANGE, "", message);
        return new ResponseEntity<>(save(user), HttpStatus.OK);
      } catch (AmqpException e) {
        log.warn("Publishing was not successful. Reverting user verification status to true. Exception: {}.", e.getMessage(), e);
        user.setVerified(true);
        user = save(user);
        return new ResponseEntity<>(user, HttpStatus.BAD_GATEWAY);
      }
    } catch (EntityNotFoundException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }
}