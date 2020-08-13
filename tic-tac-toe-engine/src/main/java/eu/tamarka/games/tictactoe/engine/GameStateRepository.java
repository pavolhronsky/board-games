package eu.tamarka.games.tictactoe.engine;

import eu.tamarka.games.tictactoe.model.GameState;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameStateRepository extends CrudRepository<GameState, String> {
  //
}