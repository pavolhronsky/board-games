package eu.tamarka.games.service.chat.message;

import eu.tamarka.games.service.chat.user.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@CrossOrigin
public class ChatMessageController {

  private final Logger log = LogManager.getLogger(this.getClass());

  private ChatMessageService chatMessageService;

  @Autowired
  public ChatMessageController(ChatMessageService chatMessageService) {
    this.chatMessageService = chatMessageService;
  }

  @MessageMapping(value = "/send/{name}/{id}")
  @SendTo(value = "/chat/games/{name}/{id}")
  public ChatMessage send(@DestinationVariable String name, @DestinationVariable String id, @Payload String messageContent,
      SimpMessageHeaderAccessor accessor) {
    log.info("Send called on destination:{}/{}. Message:{}.", name, id, messageContent);
    ChatMessage message = new ChatMessage();
    message.setAuthor((User) accessor.getUser());
    message.setGameName(name);
    message.setGameId(id);
    message.setContent(messageContent);
    message.setType(MessageType.USER);
    ChatMessage messageToSend = chatMessageService.save(message);
    log.info("Message to send:{}.", messageToSend);
    return messageToSend;
  }
}