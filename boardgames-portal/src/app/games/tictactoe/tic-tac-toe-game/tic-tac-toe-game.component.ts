import {Component, EventEmitter, Input, OnDestroy, OnInit, Output} from '@angular/core';
import {RxStompService} from '@stomp/ng2-stompjs';
import {SubscriptionType} from '../../common/subscription-type.enum';
import {Message} from '@stomp/stompjs';
import {Subscription} from 'rxjs';
import {PreGame} from '../models/pre-game';
import {GameStage} from '../../common/game-stage.enum';
import {TicTacToeToken} from '../models/tic-tac-toe-token.enum';
import {TicTacToeColor} from '../models/tic-tac-toe-color.enum';
import {PublicGame} from '../models/public-game';
import {GameModification} from '../../common/game-modification';
import {ModificationType} from '../../common/modification-type.enum';
import {TicTacToeField} from '../models/tic-tac-toe-field';
import {TicTacToeUtil} from '../tic-tac-toe-util';
import {TicTacToePlayer} from '../models/tic-tac-toe-player';
import {GoogleUser} from '../../../service/google-user';
import {UserDetailService} from '../../../service/user-detail.service';
import {LogManager} from '../../../logging/log-manager';

@Component({
  selector: 'app-tic-tac-toe-game',
  templateUrl: './tic-tac-toe-game.component.html',
  styleUrls: ['./tic-tac-toe-game.component.css']
})
export class TicTacToeGameComponent implements OnInit, OnDestroy {

  private log = LogManager.getLogger('TicTacToeGameComponent');

  private gameUserSubscription: Subscription;
  private gameSubscription: Subscription;

  @Input() gameName: string;
  @Input() gameId: string;
  @Input() private gameService: RxStompService;
  @Input() userData: any;

  @Output() private blob = new EventEmitter<string>();

  private me: GoogleUser;
  private preGame: PreGame;
  private publicGame: PublicGame;

  availableTokens = TicTacToeUtil.availableTokens();
  availableColors = TicTacToeUtil.availableColors();

  constructor(private userDetailService: UserDetailService) {
    //
  }

  ngOnInit(): void {
    this.blob.emit('url("../../../../assets/images/common/tictactoe.png")');

    this.userDetailService.getCurrentUser().subscribe(
      (user: GoogleUser) => {
        this.me = user;
      }
    );

    this.gameUserSubscription = this.gameService.watch(`/user/${this.gameName}/games/${this.gameId}`).subscribe(
      (msg: Message) => {
        const body = JSON.parse(msg.body);
        // join if not already in game
        if (!(body.players as TicTacToePlayer[]).map((p: TicTacToePlayer) => p.user.id).includes(this.userData.sub)) {
          this.log.debug('Player id: {} is joining the game.', this.userData.sub);
          this.gameService.publish({destination: `/command/join/${this.gameId}`});
        }

        if (body.stage === GameStage.CREATED) {
          this.preGame = (body as PreGame);
        } else {
          this.publicGame = (body as PublicGame);
        }
      }
    );
    this.gameSubscription = this.gameService.watch(`/${this.gameName}/games/${this.gameId}`, {type: SubscriptionType.GAME}).subscribe(
      (msg: Message) => {
        const body = JSON.parse(msg.body);
        if (body.stage === GameStage.CREATED) {
          this.publicGame = undefined;
          this.preGame = (body as PreGame);
        } else {
          this.preGame = undefined;
          this.publicGame = (body as PublicGame);
        }
      }
    );
    this.gameService.watch(`/${this.gameName}/games/${this.gameId}/modifications`).subscribe(
      (msg: Message) => {
        const gameModifications = JSON.parse(msg.body) as GameModification[];
        this.handleModifications(gameModifications);
      }
    );
    this.gameService.watch(`/user/${this.gameName}/error`).subscribe(
      (msg: Message) => {
        this.log.debug('Error: {}.', msg.body);
      }
    );
  }

  ngOnDestroy(): void {
    this.gameUserSubscription.unsubscribe();
    this.gameSubscription.unsubscribe();
  }

  public get isCreated(): boolean {
    return this.preGame && this.preGame.stage === GameStage.CREATED;
  }

  public get isInProgress(): boolean {
    return this.publicGame && this.publicGame.stage === GameStage.IN_PROGRESS;
  }

  public get isFinished(): boolean {
    return this.publicGame && this.publicGame.stage === GameStage.FINISHED;
  }

  public isMe(id: string): boolean {
    return this.me.id === id;
  }

  tokenSelected($event) {
    const token: TicTacToeToken = $event.target.value;
    this.log.debug('Selected token: {}.', token);
    if (!this.isTokenAlreadySelected(token)) {
      this.gameService.publish({destination: `/command/token/${this.gameId}`, body: $event.target.value});
    }
  }

  private isTokenAlreadySelected(token: TicTacToeToken): boolean {
    return this.preGame.players
      .map((p: TicTacToePlayer) => p.token)
      .includes(token);
  }

  colorSelected($event) {
    const color: TicTacToeColor = $event.target.value;
    this.log.debug('Selected color: {}.', color);
    if (!this.isColorAlreadySelected(color)) {
      this.gameService.publish({destination: `/command/color/${this.gameId}`, body: $event.target.value});
    }
  }

  private isColorAlreadySelected(color: TicTacToeColor) {
    return this.preGame.players
      .map((p: TicTacToePlayer) => p.color)
      .includes(color);
  }

  startGame() {
    this.gameService.publish({destination: `/command/start/${this.gameId}`});
  }

  private handleModifications(gameModifications: GameModification[]): void {
    gameModifications
      .sort((m1, m2) => m2.priority - m1.priority)
      .forEach(
        (modification: GameModification) => {
          switch (modification.type) {
            case ModificationType.MOVE: {
              const row = +modification.parameters[0];
              const column = +modification.parameters[1];
              const gameField = new TicTacToeField();
              gameField.row = row;
              gameField.column = column;
              gameField.token = TicTacToeToken[modification.parameters[2]];
              gameField.color = TicTacToeColor[modification.parameters[3]];
              this.publicGame.fields[3 * row + column] = gameField;
              break;
            }
            case ModificationType.ACTIVE_PLAYER: {
              this.publicGame.activePlayer = +modification.parameters[0];
              break;
            }
            case ModificationType.GAME_STAGE: {
              const stage = GameStage[modification.parameters[0]];
              console.log('New stage: ', stage);
              this.publicGame.stage = stage;
              break;
            }
            case ModificationType.WINNER: {
              const winnerId = +modification.parameters[0];
              this.publicGame.winner = this.publicGame.players[winnerId];
              break;
            }
            default: {
              console.log('We do not know this modification: ', modification);
            }
          }
        }
      );
  }

  get playerColors(): Map<string, string> {
    if (this.playersAreLoaded()) {
      const map = new Map<string, string>();
      const players = this.publicGame === undefined ? this.preGame.players : this.publicGame.players;

      players.forEach((p: TicTacToePlayer) => {
        map.set(p.user.id, p.color !== null ? p.color : 'silver');
      });
      return map;
    }
    return new Map<string, string>();
  }

  private playersAreLoaded(): boolean {
    return this.preGame !== undefined || this.publicGame !== undefined;
  }
}
