import {Component, OnDestroy, OnInit} from '@angular/core';
import {environment} from '../../../environments/environment';
import {OidcSecurityService} from 'angular-auth-oidc-client';
import {Router} from '@angular/router';
import {UserDetailService} from '../../service/user-detail.service';
import {GoogleUser} from '../../service/google-user';
import {Subscription} from 'rxjs';
import {LogManager} from '../../logging/log-manager';

@Component({
  selector: 'app-home',
  templateUrl: './browse.component.html',
  styleUrls: ['./browse.component.css']
})
export class BrowseComponent implements OnInit, OnDestroy {

  private log = LogManager.getLogger('BrowseComponent');

  private getCurrentUserSubscription: Subscription;

  private me: GoogleUser;
  private userDetailsChangeOpen = false;

  constructor(private oidcSecurityService: OidcSecurityService,
              private router: Router,
              private userDetailService: UserDetailService) {
    //
  }

  ngOnInit(): void {
    this.getCurrentUserSubscription = this.userDetailService.getCurrentUser().subscribe(
      (me: GoogleUser) => {
        this.log.debug('Received current user: {}.', me);
        this.me = me;
      }
    );
  }

  ngOnDestroy(): void {
    this.getCurrentUserSubscription.unsubscribe();
  }

  get getMe(): GoogleUser {
    return this.me;
  }

  get isUserDetailsChangeOpen(): boolean {
    return this.userDetailsChangeOpen;
  }

  get getGamesList(): string[] {
    return environment.games;
  }

  get getFirstInitial(): string {
    return this.me !== undefined ? this.me.nickname[0].toUpperCase() : '';
  }

  logout(): void {
    this.oidcSecurityService.logoff();
    this.router.navigate(['']);
  }

  showUserDetails() {
    this.userDetailsChangeOpen = true;
  }

  onUserUpdated(updatedUser: GoogleUser) {
    this.log.debug('User details user updated: {} is intercepted.', updatedUser);
    if (null !== updatedUser) {
      this.me = updatedUser;
    }
    this.userDetailsChangeOpen = false;
  }
}
