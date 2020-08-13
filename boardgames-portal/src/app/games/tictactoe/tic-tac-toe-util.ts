import {TicTacToeColor} from './models/tic-tac-toe-color.enum';
import {TicTacToeToken} from './models/tic-tac-toe-token.enum';

export class TicTacToeUtil {

  public static availableColors(): TicTacToeColor[] {
    return [TicTacToeColor.CRIMSON, TicTacToeColor.ROYALBLUE];
  }

  public static availableTokens(): TicTacToeToken[] {
    return [TicTacToeToken.CIRCLE, TicTacToeToken.CROSS];
  }
}
