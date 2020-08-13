import {TicTacToeColor} from './tic-tac-toe-color.enum';
import {TicTacToeToken} from './tic-tac-toe-token.enum';
import {User} from '../../common/user';

export class TicTacToePlayer {
  public user: User;
  public token: TicTacToeToken;
  public color: TicTacToeColor;
}
