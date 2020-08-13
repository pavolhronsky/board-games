import {TicTacToeColor} from './tic-tac-toe-color.enum';
import {TicTacToeToken} from './tic-tac-toe-token.enum';

export class TicTacToeField {
  public row: number;
  public column: number;
  public token: TicTacToeToken;
  public color: TicTacToeColor;
}
