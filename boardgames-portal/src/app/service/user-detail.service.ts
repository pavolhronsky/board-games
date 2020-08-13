import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {GoogleUser} from './google-user';
import {CommonHttpService} from './common-http-service';
import {environment} from '../../environments/environment';
import {PageableContent} from './pageable-content';
import {LogManager} from '../logging/log-manager';

@Injectable({
  providedIn: 'root'
})
export class UserDetailService extends CommonHttpService {

  private log = LogManager.getLogger('UserDetailService');

  public getCurrentUser(): Observable<GoogleUser> {
    this.log.debug('Getting current user.');
    return this.get<GoogleUser>(`${environment.authorizationUrl}/api/v1/me`);
  }

  public updateNickname(updatedUser: GoogleUser): Observable<GoogleUser> {
    return this.put<GoogleUser>(`${environment.authorizationUrl}/api/v1/me`, JSON.stringify(updatedUser));
  }

  public getPage(pageNumber: number, pageSize: number): Observable<PageableContent<GoogleUser>> {
    return this.get<PageableContent<GoogleUser>>(`${environment.authorizationUrl}/api/v1/users?page=${pageNumber}&size=${pageSize}`);
  }

  public verifyUser(id: string): Observable<GoogleUser> {
    return this.put<GoogleUser>(`${environment.authorizationUrl}/api/v1/users/${id}`, JSON.stringify({}));
  }

  public disproveUser(id: string): Observable<GoogleUser> {
    return this.delete<GoogleUser>(`${environment.authorizationUrl}/api/v1/users/${id}`);
  }
}
