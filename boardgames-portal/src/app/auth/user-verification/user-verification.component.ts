import {Component, OnDestroy, OnInit} from '@angular/core';
import {UserDetailService} from '../../service/user-detail.service';
import {PageableContent} from '../../service/pageable-content';
import {GoogleUser} from '../../service/google-user';
import {Subscription} from 'rxjs';
import {LogManager} from '../../logging/log-manager';

@Component({
  selector: 'app-user-verification',
  templateUrl: './user-verification.component.html',
  styleUrls: ['./user-verification.component.css']
})
export class UserVerificationComponent implements OnInit, OnDestroy {

  private log = LogManager.getLogger('UserVerificationComponent');

  private userDetailServiceSubscription: Subscription;

  private page = new PageableContent<GoogleUser>();
  private pageNumber: 0;
  private pageSize: 20;

  constructor(private userDetailService: UserDetailService) {
    //
  }

  ngOnInit(): void {
    this.getPage();
  }

  ngOnDestroy(): void {
    this.userDetailServiceSubscription.unsubscribe();
  }

  get getUserPage(): GoogleUser[] {
    return this.page.content;
  }

  get isFirst(): boolean {
    return this.page.first;
  }

  get isLast(): boolean {
    return this.page.last;
  }

  private getPage(): void {
    this.userDetailServiceSubscription = this.userDetailService.getPage(this.pageNumber, this.pageSize).subscribe(
      (page: PageableContent<GoogleUser>) => {
        this.log.debug('Data received: {}', page);
        this.page = page;
      }
    );
  }

  nextPage(): void {
    this.pageNumber += 1;
    this.getPage();
  }

  previousPage(): void {
    this.pageNumber -= 1;
    this.getPage();
  }

  verify(id: string): void {
    this.userDetailService.verifyUser(id).subscribe(
      (user: GoogleUser) => {
        this.log.debug('User verified: {}.', user);
        const idx = this.page.content.findIndex((u: GoogleUser) => u.id === user.id);
        this.page.content[idx] = user;
      }
    );
  }

  disprove(id: string): void {
    this.userDetailService.disproveUser(id).subscribe(
      (user: GoogleUser) => {
        this.log.debug('User disproved: {}.', user);
        const idx = this.page.content.findIndex((u: GoogleUser) => u.id === user.id);
        this.page.content[idx] = user;
      }
    );
  }
}
