import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, CanLoad, Route, Router, RouterStateSnapshot, UrlSegment, UrlTree} from '@angular/router';
import {Observable} from 'rxjs';
import {OidcSecurityService} from 'angular-auth-oidc-client';
import {map} from 'rxjs/operators';
import {LogManager} from '../logging/log-manager';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate, CanLoad {

  private log = LogManager.getLogger('AuthGuard');

  constructor(private router: Router,
              private oidcSecurityService: OidcSecurityService) {
    //
  }

  canLoad(route: Route, segments: UrlSegment[]): Observable<boolean> | Promise<boolean> | boolean {
    this.log.debug('AuthGuard: canLoad checked.');
    return this.checkUser();
  }

  canActivate(next: ActivatedRouteSnapshot,
              state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    this.log.debug('AuthGuard: canActivate checked.');
    return this.checkUser();
  }

  private checkUser(): Observable<boolean> {
    return this.oidcSecurityService.getIsAuthorized().pipe(
      map((isAuthorized: boolean) => {
        this.log.debug('AuthGuard: is authorized: {}.', isAuthorized);
        if (!isAuthorized) {
          this.log.debug('AuthGuard: navigating to /unauthorized.');
          this.router.navigate(['/unauthorized']);
          this.log.debug('AuthGuard: returning false.');
          return false;
        }
        this.log.debug('AuthGuard: returning true.');
        return true;
      })
    );
  }
}
