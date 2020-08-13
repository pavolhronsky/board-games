import {BrowserModule} from '@angular/platform-browser';
import {APP_INITIALIZER, NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {ForbiddenComponent} from './auth/forbidden/forbidden.component';
import {UnauthorizedComponent} from './auth/unauthorized/unauthorized.component';
import {UserDetailsModalComponent} from './auth/user-details-modal/user-details-modal.component';
import {GamesListComponent} from './games/common/games-list/games-list.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {AuthModule, ConfigResult, OidcConfigService, OidcSecurityService, OpenIdConfiguration} from 'angular-auth-oidc-client';
import {environment} from '../environments/environment';
import {BrowseComponent} from './common/browse/browse.component';
import {SearchComponent} from './common/search/search.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {BearerTokenInterceptor} from './auth/bearer-token-interceptor';
import {GameTileComponent} from './games/common/game-tile/game-tile.component';
import {TicTacToeGameComponent} from './games/tictactoe/tic-tac-toe-game/tic-tac-toe-game.component';
import {TicTacToeBoardComponent} from './games/tictactoe/tic-tac-toe-board/tic-tac-toe-board.component';
import {TicTacToeFieldComponent} from './games/tictactoe/tic-tac-toe-field/tic-tac-toe-field.component';
import {CircleComponent} from './games/tictactoe/models/circle/circle.component';
import {DiagonalCrossComponent} from './games/tictactoe/models/diagonal-cross/diagonal-cross.component';
import {GameFactoryComponent} from './games/common/game-factory/game-factory.component';
import {ChatWindowComponent} from './chat/chat-window/chat-window.component';
import {ChatMessageComponent} from './chat/chat-message/chat-message.component';
import {IntroComponent} from './common/intro/intro.component';
import {CreateGameModalComponent} from './games/common/create-game-modal/create-game-modal.component';
import {UserVerificationComponent} from './auth/user-verification/user-verification.component';
import {LogManager} from './logging/log-manager';

export function loadConfig(oidcConfigService: OidcConfigService) {
  console.log('APP_INITIALIZER STARTING');
  return () => oidcConfigService.load_using_stsServer('https://accounts.google.com');
}

@NgModule({
  declarations: [
    AppComponent,
    ForbiddenComponent,
    UnauthorizedComponent,
    UserDetailsModalComponent,
    GamesListComponent,
    BrowseComponent,
    SearchComponent,
    GameTileComponent,
    TicTacToeGameComponent,
    TicTacToeBoardComponent,
    TicTacToeFieldComponent,
    CircleComponent,
    DiagonalCrossComponent,
    GameFactoryComponent,
    ChatWindowComponent,
    ChatMessageComponent,
    IntroComponent,
    CreateGameModalComponent,
    UserVerificationComponent
  ],
  imports: [
    AuthModule.forRoot(),
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [
    OidcSecurityService,
    OidcConfigService,
    {
      provide: APP_INITIALIZER,
      useFactory: loadConfig,
      deps: [OidcConfigService],
      multi: true
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: BearerTokenInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
  private log = LogManager.getLogger('AppModule');

  constructor(private oidcSecurityService: OidcSecurityService,
              private oidcConfigService: OidcConfigService) {
    this.oidcConfigService.onConfigurationLoaded.subscribe((configResult: ConfigResult) => {
      const config: OpenIdConfiguration = {
        stsServer: 'https://accounts.google.com',
        redirect_url: environment.redirectUrl,
        client_id: '337993859470-jp6vs24sge0sqr8jrlcbppvsvud85mqq.apps.googleusercontent.com',
        response_type: 'id_token token',
        scope: 'openid email profile',
        // trigger_authorization_result_event: true,
        post_logout_redirect_uri: environment.redirectUrl + '/unauthorized',
        // start_checksession: false,
        silent_renew: false,
        silent_renew_url: 'https://localhost:44386/silent-renew.html',
        post_login_route: '/browse',
        forbidden_route: '/forbidden',
        unauthorized_route: '/unauthorized',
        log_console_warning_active: true,
        log_console_debug_active: true,
        max_id_token_iat_offset_allowed_in_seconds: 30,
        history_cleanup_off: true
      };
      this.oidcSecurityService.setupModule(config, configResult.authWellknownEndpoints);
    });

    this.log.debug('APP STARTING');
  }
}
