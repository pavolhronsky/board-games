// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

import {LogLevel} from '../app/logging/log-level.enum';

export const environment = {
  production: false,
  logLevel: LogLevel.DEBUG,
  // authentication and authorization service endpoint
  authorizationUrl: 'https://authorization.docker.dev',
  redirectUrl: 'https://localhost:4200',
  // chat service endpoints
  chatUrl: 'https://chat.docker.dev',
  chatUrlWs: 'wss://chat.docker.dev/ws',
  games: [
    'https://tictactoe.docker.dev'
  ] as string[],
  engines: {
    tictactoe: 'wss://tictactoe.docker.dev/ws'
  }
};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */

// import 'zone.js/dist/zone-error';  // Included with Angular CLI.
