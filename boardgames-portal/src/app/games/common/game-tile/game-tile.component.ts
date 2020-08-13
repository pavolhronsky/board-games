import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {GameDescription} from './game-description';
import {CommonHttpService} from '../../../service/common-http-service';
import {Subscription} from 'rxjs';

@Component({
  selector: 'app-game-tile',
  templateUrl: './game-tile.component.html',
  styleUrls: ['./game-tile.component.css']
})
export class GameTileComponent implements OnInit, OnDestroy {

  private gameDescriptionSubscription: Subscription;

  @Input() public gameUrl: string;

  private gameDescription: GameDescription;
  private available = false;

  constructor(private http: CommonHttpService) {
    //
  }

  ngOnInit(): void {
    this.gameDescriptionSubscription = this.http.get<GameDescription>(`${this.gameUrl}/game-description`).subscribe(
      (gameDescription: GameDescription) => {
        this.gameDescription = gameDescription;
        this.available = true;
      },
      err => {
        this.available = false;
      }
    );
  }

  ngOnDestroy(): void {
    this.gameDescriptionSubscription.unsubscribe();
  }

  get getGameDescription(): GameDescription {
    return this.gameDescription;
  }
}
