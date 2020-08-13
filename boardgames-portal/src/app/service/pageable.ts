import {Sort} from './sort';

export class Pageable {
  public sort: Sort;
  public pageSize: number;
  public pageNumber: number;
  public offset: number;
  public unpaged: boolean;
  public paged: boolean;
}
