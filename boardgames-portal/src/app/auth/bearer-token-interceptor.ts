import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs';
import {OidcSecurityService} from 'angular-auth-oidc-client';
import {Injectable, Injector} from '@angular/core';

@Injectable()
export class BearerTokenInterceptor implements HttpInterceptor {
  private oidcSecurityService: OidcSecurityService;

  constructor(private injector: Injector) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let requestToForward = req;

    if (req.urlWithParams === 'https://openidconnect.googleapis.com/v1/userinfo') {
      return next.handle(requestToForward);
    }

    if (this.oidcSecurityService === undefined) {
      this.oidcSecurityService = this.injector.get(OidcSecurityService);
    }
    if (this.oidcSecurityService !== undefined) {
      const token = this.oidcSecurityService.getIdToken();
      if (token !== '') {
        const tokenValue = 'Bearer ' + token;
        requestToForward = req.clone({setHeaders: {Authorization: tokenValue}});
      }
    } else {
      console.log('OidcSecurityService undefined: NO auth header!');
    }

    return next.handle(requestToForward);
  }
}
