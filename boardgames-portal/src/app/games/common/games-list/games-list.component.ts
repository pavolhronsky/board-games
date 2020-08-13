import {Component, OnDestroy, OnInit} from '@angular/core';
import {RxStompService} from '@stomp/ng2-stompjs';
import {ActivatedRoute, Router} from '@angular/router';
import {environment} from '../../../../environments/environment';
import {OidcSecurityService} from 'angular-auth-oidc-client';
import {Subscription} from 'rxjs';
import {Message} from '@stomp/stompjs';
import {SubscriptionType} from '../subscription-type.enum';
import {GamesListEntry} from './games-list-entry';
import {TicTacToePlayer} from '../../tictactoe/models/tic-tac-toe-player';
import {LogManager} from '../../../logging/log-manager';

@Component({
  selector: 'app-game-list',
  templateUrl: './games-list.component.html',
  styleUrls: ['./games-list.component.css'],
  providers: [RxStompService]
})
export class GamesListComponent implements OnInit, OnDestroy {

  private log = LogManager.getLogger('GameListComponent');

  private gamesListUserSubscription: Subscription;
  private gamesListSubscription: Subscription;

  private gameName: string;
  private games: GamesListEntry[] = [];
  private createGameModalOpen = false;

  constructor(private router: Router,
              private route: ActivatedRoute,
              private oidcSecurityService: OidcSecurityService,
              private gamesListService: RxStompService) {
    //
  }

  ngOnInit(): void {
    this.gameName = this.route.snapshot.paramMap.get('game-name');
    this.oidcSecurityService.getUserData().subscribe(
      (userData: any) => {
        this.gamesListService.configure({
          brokerURL: environment.engines[this.gameName],
          connectHeaders: {
            login: '',
            passcode: '',
            userId: userData.sub
          },
          heartbeatIncoming: 0,
          heartbeatOutgoing: 20000,
          reconnectDelay: 1000,
          debug: str => {
            this.log.debug(str);
          },
        });
        this.gamesListService.activate();

        this.gamesListUserSubscription = this.gamesListService.watch(`/user/${this.gameName}/games`).subscribe(
          (msg: Message) => {
            this.games = (JSON.parse(msg.body) as GamesListEntry[]);
            this.log.debug('Games: {}.', this.games);
          }
        );
        this.gamesListSubscription = this.gamesListService.watch(`/${this.gameName}/games`, {type: SubscriptionType.LIST}).subscribe(
          (msg: Message) => {
            this.log.debug('New game: {}.', msg.body);
            this.games = (JSON.parse(msg.body) as GamesListEntry[]);
          }
        );
        this.gamesListSubscription = this.gamesListService.watch(`/user/${this.gameName}/created`).subscribe(
          (msg: Message) => {
            this.log.debug('Game created. ID={}.', msg.body);
            this.router.navigate([`/games/${this.gameName}/${msg.body}`]);
          }
        );
      }
    );
  }

  ngOnDestroy(): void {
    this.gamesListUserSubscription.unsubscribe();
    this.gamesListSubscription.unsubscribe();
    this.gamesListSubscription.unsubscribe();
  }

  get getGames(): GamesListEntry[] {
    return this.games;
  }

  getPlayerNames(game: GamesListEntry): string {
    return game.players.map((p: TicTacToePlayer) => p.user.name).join(', ');
  }

  get isCreateGameModalOpen(): boolean {
    return this.createGameModalOpen;
  }

  openCreateGameModal() {
    this.createGameModalOpen = true;
  }

  onNewGameName(newGameName: string) {
    this.log.debug('Receiving new game name: {}.', newGameName);
    if (null !== newGameName) {
      this.gamesListService.publish({destination: `/command/create`, body: newGameName});
    }
    this.createGameModalOpen = false;
  }
}
