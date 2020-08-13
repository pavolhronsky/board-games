import {Component, Input} from '@angular/core';

@Component({
  selector: 'app-diagonal-cross',
  templateUrl: './diagonal-cross.component.html',
  styleUrls: ['./diagonal-cross.component.css']
})
export class DiagonalCrossComponent {
  @Input() color: string;
}
