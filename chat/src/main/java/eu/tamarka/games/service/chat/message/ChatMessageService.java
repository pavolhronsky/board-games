package eu.tamarka.games.service.chat.message;

import eu.tamarka.games.service.chat.jpa.JpaService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatMessageService extends JpaService<ChatMessage, Long, ChatMessageRepository> {

  @Autowired
  public ChatMessageService(ChatMessageRepository repository) {
    super(repository);
  }

  public List<ChatMessage> findAllByGameNameAndGameId(String gameName, String gameId) {
    return getRepository().findAllByGameNameAndGameId(gameName, gameId);
  }
}