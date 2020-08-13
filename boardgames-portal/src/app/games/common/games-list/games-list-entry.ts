import {GameStage} from '../game-stage.enum';
import {GenericPlayer} from './generic-player';

export class GamesListEntry {
  public id: string;
  public name: string;
  public stage: GameStage;
  public players: GenericPlayer[];
}
