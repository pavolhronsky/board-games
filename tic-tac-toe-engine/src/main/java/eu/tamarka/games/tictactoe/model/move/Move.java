package eu.tamarka.games.tictactoe.model.move;

import eu.tamarka.games.tictactoe.model.GameState;
import java.util.List;

public interface Move {

  boolean canPlay(GameState game);

  List<GameModification> execute(GameState game);
}