import {GameStage} from '../../common/game-stage.enum';
import {TicTacToePlayer} from './tic-tac-toe-player';

export class PreGame {
  public id: string;
  public name: string;
  public stage: GameStage;
  public players: TicTacToePlayer[];
}
