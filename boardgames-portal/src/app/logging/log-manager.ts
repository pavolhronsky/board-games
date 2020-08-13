import {LogLevel} from './log-level.enum';
import {environment} from '../../environments/environment';
import {Logger} from './logger';
import {ConsoleLogger} from './console-logger';

export class LogManager {
  private static logLevel: LogLevel = environment.logLevel;

  private static loggers: Map<string, Logger> = new Map<string, Logger>();

  static getLogger(componentName: string): Logger {
    if (this.loggers.has(componentName)) {
      return this.loggers.get(componentName);
    }
    const logger = new ConsoleLogger(this.logLevel, componentName);
    this.loggers.set(componentName, logger);
    return logger;
  }
}
