import {LogLevel} from './log-level.enum';
import {AbstractLogger} from './abstract-logger';
import * as moment from 'moment';

export class ConsoleLogger extends AbstractLogger {

  constructor(private logLevel: LogLevel, private componentName: string) {
    super(logLevel);
  }

  fatal(msg: string, ...inputs: any[]): void {
    if (this.isFatalEnabled()) {
      this.log(LogLevel.FATAL, msg, inputs);
    }
  }

  error(msg: string, ...inputs: any[]): void {
    if (this.isErrorEnabled()) {
      this.log(LogLevel.ERROR, msg, inputs);
    }
  }

  warn(msg: string, ...inputs: any[]): void {
    if (this.isWarnEnabled()) {
      this.log(LogLevel.WARN, msg, inputs);
    }
  }

  info(msg: string, ...inputs: any[]): void {
    if (this.isInfoEnabled()) {
      this.log(LogLevel.INFO, msg, inputs);
    }
  }

  debug(msg: string, ...inputs: any[]): void {
    if (this.isDebugEnabled()) {
      this.log(LogLevel.DEBUG, msg, inputs);
    }
  }

  trace(msg: string, ...inputs: any[]): void {
    if (this.isTraceEnabled()) {
      this.log(LogLevel.TRACE, msg, inputs);
    }
  }

  private log(level: LogLevel, msg: string, inputs: any[]): void {
    let message = msg;
    for (const input of inputs) {
      message = message.replace('{}', JSON.stringify(input));
    }
    console.log(`${moment().format()} [${LogLevel[level]}] ${this.componentName} - ${message}`);
  }
}
