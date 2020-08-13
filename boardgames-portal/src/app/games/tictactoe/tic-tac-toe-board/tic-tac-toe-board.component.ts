import {Component, Input} from '@angular/core';
import {PublicGame} from '../models/public-game';
import {RxStompService} from '@stomp/ng2-stompjs';
import {TicTacToePlayer} from '../models/tic-tac-toe-player';

@Component({
  selector: 'app-game-board',
  templateUrl: './tic-tac-toe-board.component.html',
  styleUrls: ['./tic-tac-toe-board.component.css']
})
export class TicTacToeBoardComponent {
  @Input() gameService: RxStompService;
  @Input() publicGame: PublicGame;
  @Input() gameId: string;

  public isActivePlayer(player: TicTacToePlayer): boolean {
    console.log('Is Active player check: ', this.publicGame, ' player: ', player);
    return player.user.id === this.publicGame.players[this.publicGame.activePlayer].user.id;
  }
}
