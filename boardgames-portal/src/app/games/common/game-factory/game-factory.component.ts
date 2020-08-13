import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {OidcSecurityService} from 'angular-auth-oidc-client';
import {RxStompService} from '@stomp/ng2-stompjs';
import {environment} from '../../../../environments/environment';
import {Subscription} from 'rxjs';

@Component({
  selector: 'app-game-factory',
  templateUrl: './game-factory.component.html',
  styleUrls: ['./game-factory.component.css'],
  providers: [RxStompService]
})
export class GameFactoryComponent implements OnInit, OnDestroy {

  private getUserDataSubscription: Subscription;

  private gameBackgroundImage = 'none';

  public gameName: string;
  public gameId: string;
  public userData: any;

  constructor(private router: Router,
              private route: ActivatedRoute,
              private oidcSecurityService: OidcSecurityService,
              public gameService: RxStompService) {
  }

  ngOnInit(): void {
    this.gameName = this.route.snapshot.paramMap.get('game-name');
    this.gameId = this.route.snapshot.paramMap.get('game-id');

    this.getUserDataSubscription = this.oidcSecurityService.getUserData().subscribe(
      (userData: any) => {
        this.userData = userData;
        this.gameService.configure({
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
            console.log(str);
          }
        });
        this.gameService.activate();
      }
    );
  }

  get isTicTacToeGame(): boolean {
    return this.gameName === 'tictactoe';
  }

  get getGameBackgroundImage(): string {
    return this.gameBackgroundImage;
  }

  ngOnDestroy(): void {
    this.getUserDataSubscription.unsubscribe();
    this.gameService.deactivate();
  }

  goBack() {
    this.router.navigate([`/games/${this.gameName}`]);
  }

  onGameBackgroundImageSet(gameBackgroundImage: string) {
    this.gameBackgroundImage = gameBackgroundImage;
  }
}
