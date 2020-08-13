import {Component, OnDestroy, OnInit} from '@angular/core';
import {OidcSecurityService} from 'angular-auth-oidc-client';
import {Subscription} from 'rxjs';
import {Router} from '@angular/router';
import {LogManager} from '../../logging/log-manager';

@Component({
  selector: 'app-intro',
  templateUrl: './intro.component.html',
  styleUrls: ['./intro.component.css']
})
export class IntroComponent implements OnInit, OnDestroy {
  private log = LogManager.getLogger('IntroComponent');
  private getIsAuthorizedSubscription: Subscription;

  constructor(private oidcSecurityService: OidcSecurityService,
              private router: Router) {
    if (this.oidcSecurityService.moduleSetup) {
      this.doCallbackLogicIfRequired();
    } else {
      this.oidcSecurityService.onModuleSetup.subscribe(() => {
        this.doCallbackLogicIfRequired();
      });
    }
  }

  ngOnInit(): void {
    this.log.debug('Entering intro component.');
    this.getIsAuthorizedSubscription = this.oidcSecurityService.getIsAuthorized().subscribe(
      (isAuthorized: boolean) => {
        this.log.debug('Is authorized? {}.', isAuthorized);
        if (isAuthorized) {
          this.log.debug('We are authorized. Redirecting to /games.');
          this.router.navigate(['browse']);
        }
      }
    );
  }

  ngOnDestroy(): void {
    this.getIsAuthorizedSubscription.unsubscribe();
  }

  private doCallbackLogicIfRequired() {
    if (window.location.hash) {
      this.oidcSecurityService.authorizedImplicitFlowCallback();
    }
  }

  login() {
    this.oidcSecurityService.authorize();
  }
}
