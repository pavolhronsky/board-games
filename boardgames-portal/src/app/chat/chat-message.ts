import {ChatUser} from './chat-user';
import {MessageType} from './message-type.enum';

export class ChatMessage {
  public id: number;
  public author: ChatUser;
  public gameName: string;
  public gameId: string;
  public content: string;
  public type: MessageType;
  public createdAt: string;
}
