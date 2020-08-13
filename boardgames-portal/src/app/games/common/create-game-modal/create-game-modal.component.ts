import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormBuilder, FormControl, Validators} from '@angular/forms';
import {LogManager} from '../../../logging/log-manager';

@Component({
  selector: 'app-create-game-modal',
  templateUrl: './create-game-modal.component.html',
  styleUrls: ['./create-game-modal.component.css']
})
export class CreateGameModalComponent implements OnInit {

  private log = LogManager.getLogger('CreateGameModalComponent');

  @Output() private newGameName = new EventEmitter<string>();

  newGameNameControl: FormControl;

  constructor(private fb: FormBuilder) {
  }

  ngOnInit(): void {
    this.newGameNameControl = this.fb.control('', Validators.required);
  }

  create(): void {
    if (this.newGameNameControl.valid) {
      this.newGameName.emit(this.newGameNameControl.value);
      this.newGameNameControl.setValue('');
    }
  }

  cancel(): void {
    this.newGameNameControl.setValue('');
    this.newGameName.emit(null);
  }
}
