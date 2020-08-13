import {Component, Input} from '@angular/core';
import {TicTacToeField} from '../models/tic-tac-toe-field';
import {RxStompService} from '@stomp/ng2-stompjs';
import {TicTacToeToken} from '../models/tic-tac-toe-token.enum';

@Component({
  selector: 'app-game-field',
  templateUrl: './tic-tac-toe-field.component.html',
  styleUrls: ['./tic-tac-toe-field.component.css']
})
export class TicTacToeFieldComponent {

  @Input() gameService: RxStompService;
  @Input() field: TicTacToeField;
  @Input() gameId: string;

  makeMove() {
    this.gameService.publish({
      destination: `/command/move/${this.gameId}`,
      body: JSON.stringify({row: this.field.row, column: this.field.column})
    });
  }

  isCircle() {
    return this.field.token === TicTacToeToken.CIRCLE;
  }

  isDiagonalCross() {
    return this.field.token === TicTacToeToken.CROSS;
  }
}
