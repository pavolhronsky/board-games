import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ForbiddenComponent} from './auth/forbidden/forbidden.component';
import {UnauthorizedComponent} from './auth/unauthorized/unauthorized.component';
import {BrowseComponent} from './common/browse/browse.component';
import {IntroComponent} from './common/intro/intro.component';
import {AuthGuard} from './auth/auth.guard';
import {GamesListComponent} from './games/common/games-list/games-list.component';
import {GameFactoryComponent} from './games/common/game-factory/game-factory.component';
import {UserVerificationComponent} from './auth/user-verification/user-verification.component';
import {UserVerifiedGuard} from './auth/user-verified.guard';

const routes: Routes = [
  {path: '', component: IntroComponent},
  {path: 'unauthorized', component: UnauthorizedComponent},
  {path: 'forbidden', component: ForbiddenComponent},
  {
    path: 'browse',
    component: BrowseComponent,
    canActivate: [AuthGuard, UserVerifiedGuard],
    canLoad: [AuthGuard, UserVerifiedGuard]
  },
  {
    path: 'games/:game-name',
    component: GamesListComponent,
    canActivate: [AuthGuard, UserVerifiedGuard],
    canLoad: [AuthGuard, UserVerifiedGuard]
  },
  {
    path: 'games/:game-name/:game-id',
    component: GameFactoryComponent,
    canActivate: [AuthGuard, UserVerifiedGuard],
    canLoad: [AuthGuard, UserVerifiedGuard]
  },
  {
    path: 'verify',
    component: UserVerificationComponent,
    // canActivate: [AuthGuard, UserVerifiedGuard],
    canLoad: [AuthGuard, UserVerifiedGuard]
  }

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
  //
}
