package eu.tamarka.games.tictactoe.service;

import java.security.Principal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
public class ErrorService {

  private final Logger log = LogManager.getLogger(this.getClass());

  private SimpMessageSendingOperations template;

  @Autowired
  public ErrorService(SimpMessageSendingOperations template) {
    this.template = template;
  }

  public void send(Principal principal, String message) {
    log.debug("Error message.\n\tRecipient: {}\n\tMessage: {}\n", principal.getName(), message);
    template.convertAndSendToUser(principal.getName(), "/tictactoe/error", message);
  }
}
