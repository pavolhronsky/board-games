import {TicTacToePlayer} from './tic-tac-toe-player';
import {GameStage} from '../../common/game-stage.enum';
import {TicTacToeField} from './tic-tac-toe-field';

export class PublicGame {
  public id: string;
  public name: string;
  public fields: TicTacToeField[];
  public players: TicTacToePlayer[];
  public stage: GameStage;
  public activePlayer: number;
  public winner: TicTacToePlayer;
}
