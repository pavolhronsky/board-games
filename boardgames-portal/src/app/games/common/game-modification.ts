import {ModificationType} from './modification-type.enum';

export class GameModification {
  public type: ModificationType;
  public priority: number;
  public parameters: string[];
}
