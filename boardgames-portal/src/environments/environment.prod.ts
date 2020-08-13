import {LogLevel} from '../app/logging/log-level.enum';

export const environment = {
  production: true,
  logLevel: LogLevel.DEBUG,
  // authentication and authorization service endpoint
  authorizationUrl: 'https://authorization.tamarka.eu',
  redirectUrl: 'https://games.tamarka.eu',
  // chat service endpoints
  chatUrl: 'https://chat.tamarka.eu',
  chatUrlWs: 'wss://chat.tamarka.eu/ws',
  games: [
    'https://tictactoe.tamarka.eu'
  ] as string[],
  engines: {
    tictactoe: 'wss://tictactoe.tamarka.eu/ws'
  }
};
