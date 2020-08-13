package eu.tamarka.games.service.chat.message;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

  List<ChatMessage> findAllByGameNameAndGameId(String gameName, String gameId);
}