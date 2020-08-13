import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DiagonalCrossComponent } from './diagonal-cross.component';

describe('DiagonalCrossComponent', () => {
  let component: DiagonalCrossComponent;
  let fixture: ComponentFixture<DiagonalCrossComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DiagonalCrossComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DiagonalCrossComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
