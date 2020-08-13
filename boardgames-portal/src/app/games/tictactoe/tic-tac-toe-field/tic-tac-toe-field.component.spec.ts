import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TicTacToeFieldComponent } from './tic-tac-toe-field.component';

describe('TicTacToeFieldComponent', () => {
  let component: TicTacToeFieldComponent;
  let fixture: ComponentFixture<TicTacToeFieldComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TicTacToeFieldComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TicTacToeFieldComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
