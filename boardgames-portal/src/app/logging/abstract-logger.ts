import {Logger} from './logger';
import {LogLevel} from './log-level.enum';

export abstract class AbstractLogger implements Logger {

  protected constructor(private level: LogLevel) {
    //
  }

  abstract fatal(msg: string, ...inputs: any[]): void;

  abstract warn(msg: string, ...inputs: any[]): void;

  abstract error(msg: string, ...inputs: any[]): void;

  abstract info(msg: string, ...inputs: any[]): void;

  abstract debug(msg: string, ...inputs: any[]): void;

  abstract trace(msg: string, ...inputs: any[]): void;

  isFatalEnabled(): boolean {
    return LogLevel.FATAL <= this.level;
  }

  isErrorEnabled(): boolean {
    return LogLevel.ERROR <= this.level;
  }

  isWarnEnabled(): boolean {
    return LogLevel.WARN <= this.level;
  }

  isInfoEnabled(): boolean {
    return LogLevel.INFO <= this.level;
  }

  isDebugEnabled(): boolean {
    return LogLevel.DEBUG <= this.level;
  }

  isTraceEnabled(): boolean {
    return LogLevel.TRACE <= this.level;
  }
}
