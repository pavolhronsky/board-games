package eu.tamarka.games.service.chat.message;

import eu.tamarka.games.service.chat.user.User;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table
@EntityListeners(AuditingEntityListener.class)
public class ChatMessage {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(
      name = "userId"
  )
  private User author;

  @Column(nullable = false)
  private String gameName;

  @Column(nullable = false)
  private String gameId;

  @Column(nullable = false)
  private String content;

  @Column(nullable = false)
  private MessageType type;

  @CreatedDate
  @Column(nullable = false)
  private LocalDateTime createdAt;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public User getAuthor() {
    return author;
  }

  public void setAuthor(User author) {
    this.author = author;
  }

  public String getGameName() {
    return gameName;
  }

  public void setGameName(String gameName) {
    this.gameName = gameName;
  }

  public String getGameId() {
    return gameId;
  }

  public void setGameId(String gameId) {
    this.gameId = gameId;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public MessageType getType() {
    return type;
  }

  public void setType(MessageType type) {
    this.type = type;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  @Override
  public String toString() {
    return "{" +
        "\"id\":" + (id == null ? "null" : "\"" + id + "\"") + ", " +
        "\"author\":" + (author == null ? "null" : author) + ", " +
        "\"gameName\":" + (gameName == null ? "null" : "\"" + gameName + "\"") + ", " +
        "\"gameId\":" + (gameId == null ? "null" : "\"" + gameId + "\"") + ", " +
        "\"content\":" + (content == null ? "null" : "\"" + content + "\"") + ", " +
        "\"type\":" + (type == null ? "null" : "\"" + type.toString() + "\"") + ", " +
        "\"createdAt\":" + (createdAt == null ? "null" : "\"" + createdAt + "\"") +
        "}";
  }
}