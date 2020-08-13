package eu.tamarka.games.tictactoe.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:tictactoe.properties")
@ConfigurationProperties(prefix = "tictactoe")
public class GameDescription {

  private String name;

  private String description;

  private String playingTime;

  private Integer minNumberOfPlayers;

  private Integer maxNumberOfPlayers;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getPlayingTime() {
    return playingTime;
  }

  public void setPlayingTime(String playingTime) {
    this.playingTime = playingTime;
  }

  public Integer getMinNumberOfPlayers() {
    return minNumberOfPlayers;
  }

  public void setMinNumberOfPlayers(Integer minNumberOfPlayers) {
    this.minNumberOfPlayers = minNumberOfPlayers;
  }

  public Integer getMaxNumberOfPlayers() {
    return maxNumberOfPlayers;
  }

  public void setMaxNumberOfPlayers(Integer maxNumberOfPlayers) {
    this.maxNumberOfPlayers = maxNumberOfPlayers;
  }
}