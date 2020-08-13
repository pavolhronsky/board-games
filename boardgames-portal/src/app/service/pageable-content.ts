import {Sort} from './sort';
import {Pageable} from './pageable';

export class PageableContent<T> {
  public content: T[] = [];
  public pageable: Pageable;
  public totalPages: number;
  public totalElements: number;
  public last: boolean;
  public first: boolean;
  public sort: Sort;
  public numberOfElements: number;
  public size: number;
  public number: number;
  public empty: boolean;
}
