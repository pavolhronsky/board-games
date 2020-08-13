export interface Logger {

  fatal(msg: string, ...inputs: any[]): void;

  error(msg: string, ...inputs: any[]): void;

  warn(msg: string, ...inputs: any[]): void;

  info(msg: string, ...inputs: any[]): void;

  debug(msg: string, ...inputs: any[]): void;

  trace(msg: string, ...inputs: any[]): void;
}
