package eu.tamarka.games.tictactoe.engine;

import eu.tamarka.games.tictactoe.model.GameField;
import eu.tamarka.games.tictactoe.model.GameState;
import org.springframework.stereotype.Component;

@Component
public class TicTacToeRules {

  public boolean isWin(GameState state) {
    GameField[] fields = state.getFields();

    if (fields[0].getToken() != null && fields[0].getToken() == fields[1].getToken() && fields[0].getToken() == fields[2].getToken()) {
      return true;
    }

    if (fields[3].getToken() != null && fields[3].getToken() == fields[4].getToken() && fields[3].getToken() == fields[5].getToken()) {
      return true;
    }

    if (fields[6].getToken() != null && fields[6].getToken() == fields[7].getToken() && fields[6].getToken() == fields[8].getToken()) {
      return true;
    }

    if (fields[0].getToken() != null && fields[0].getToken() == fields[3].getToken() && fields[0].getToken() == fields[6].getToken()) {
      return true;
    }

    if (fields[1].getToken() != null && fields[1].getToken() == fields[4].getToken() && fields[1].getToken() == fields[7].getToken()) {
      return true;
    }

    if (fields[2].getToken() != null && fields[2].getToken() == fields[5].getToken() && fields[2].getToken() == fields[8].getToken()) {
      return true;
    }

    if (fields[0].getToken() != null && fields[0].getToken() == fields[4].getToken() && fields[0].getToken() == fields[8].getToken()) {
      return true;
    }

    if (fields[2].getToken() != null && fields[2].getToken() == fields[4].getToken() && fields[2].getToken() == fields[6].getToken()) {
      return true;
    }

    return false;
  }

  public boolean isOver(GameState state) {
    for (GameField field : state.getFields()) {
      if (field.getToken() == null) {
        return false;
      }
    }
    return true;
  }
}