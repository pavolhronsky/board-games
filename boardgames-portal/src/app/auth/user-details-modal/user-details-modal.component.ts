import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {UserDetailService} from '../../service/user-detail.service';
import {GoogleUser} from '../../service/google-user';
import {Subscription} from 'rxjs';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {LogManager} from '../../logging/log-manager';

@Component({
  selector: 'app-user-details',
  templateUrl: './user-details-modal.component.html',
  styleUrls: ['./user-details-modal.component.css']
})
export class UserDetailsModalComponent implements OnInit {

  private log = LogManager.getLogger('UserDetailsModalComponent');

  private updateNicknameSubscription: Subscription;

  @Input() private user: GoogleUser;
  @Output() private updatedUser = new EventEmitter<GoogleUser>();

  userDetails: FormGroup;

  constructor(private fb: FormBuilder,
              private userDetailService: UserDetailService) {
    //
  }

  ngOnInit(): void {
    this.userDetails = this.fb.group({
      nickname: this.fb.control(this.user.nickname, Validators.required)
    });
    this.log.debug('User details init form group: {}.', (this.userDetails.value as GoogleUser));
  }

  get getUser(): GoogleUser {
    return this.user;
  }

  update(): void {
    this.log.debug('Updating user;');
    const updatedUser = new GoogleUser();
    updatedUser.id = this.user.id;
    updatedUser.nickname = this.userDetails.get('nickname').value;
    this.updateNicknameSubscription = this.userDetailService.updateNickname(updatedUser).subscribe(
      (user: GoogleUser) => {
        this.user = user;
        this.updatedUser.emit(user);
        this.updateNicknameSubscription.unsubscribe();
      }
    );
  }

  cancel() {
    this.userDetails.get('nickname').setValue(this.user.nickname);
    this.updatedUser.emit(null);
  }
}
