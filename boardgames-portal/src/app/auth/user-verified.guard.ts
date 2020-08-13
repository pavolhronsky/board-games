import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, CanLoad, Route, Router, RouterStateSnapshot, UrlSegment, UrlTree} from '@angular/router';
import {Observable} from 'rxjs';
import {UserDetailService} from '../service/user-detail.service';
import {map} from 'rxjs/operators';
import {GoogleUser} from '../service/google-user';
import {LogManager} from '../logging/log-manager';

@Injectable({
  providedIn: 'root'
})
export class UserVerifiedGuard implements CanActivate, CanLoad {

  private log = LogManager.getLogger('UserVerifiedGuard');

  constructor(private router: Router,
              private userDetailService: UserDetailService) {
    //
  }

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    return this.isUserVerified();
  }

  canLoad(
    route: Route,
    segments: UrlSegment[]): Observable<boolean> | Promise<boolean> | boolean {
    return this.isUserVerified();
  }

  private isUserVerified(): Observable<boolean> {
    return this.userDetailService.getCurrentUser().pipe(
      map((user: GoogleUser) => {
          const isVerified = user.verified;
          this.log.debug('UserVerifiedGuard: User verification check: {}.', user);
          if (!isVerified) {
            this.log.debug('UserVerifiedGuard: navigating to /forbidden.');
            this.router.navigate(['/forbidden']);
            this.log.debug('UserVerifiedGuard: returning false.');
            return false;
          }
          this.log.debug('UserVerifiedGuard: returning true.');
          return true;
        }
      )
    );
  }
}
