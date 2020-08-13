package eu.tamarka.games.tictactoe;

import eu.tamarka.games.tictactoe.config.GameDescription;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(GameDescription.class)
public class TicTacToeApplication {

  public static void main(String[] args) {
    SpringApplication.run(TicTacToeApplication.class, args);
  }
}