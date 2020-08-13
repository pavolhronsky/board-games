import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class CommonHttpService {

  readonly getHeaders: HttpHeaders;
  readonly putHeaders: HttpHeaders;
  readonly deleteHeaders: HttpHeaders;

  constructor(private http: HttpClient) {
    this.getHeaders = new HttpHeaders({'Content-Type': 'application/json;charset=UTF-8'});
    this.putHeaders = this.getHeaders.append('Accept', 'application/json;charset=UTF-8');
    this.deleteHeaders = new HttpHeaders({'Content-Type': 'application/json;charset=UTF-8'});
  }

  public get<T>(url: string): Observable<T> {
    return this.http.get<T>(url, {headers: this.getHeaders});
  }

  public put<T>(url: string, dataJson: string): Observable<T> {
    return this.http.put<T>(url, dataJson, {headers: this.putHeaders});
  }

  public delete<T>(url: string): Observable<T> {
    return this.http.delete<T>(url, {headers: this.deleteHeaders});
  }
}
