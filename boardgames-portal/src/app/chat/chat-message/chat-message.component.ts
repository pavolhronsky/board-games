import {Component, Input} from '@angular/core';
import {ChatMessage} from '../chat-message';
import {MessageType} from '../message-type.enum';

@Component({
  selector: 'app-chat-message',
  templateUrl: './chat-message.component.html',
  styleUrls: ['./chat-message.component.css']
})
export class ChatMessageComponent {

  @Input()
  message: ChatMessage;

  get isConnectionMessage(): boolean {
    return this.message.type === MessageType.CONNECTION;
  }

  get isUserMessage(): boolean {
    return this.message.type === MessageType.USER;
  }

  get isGameMessage(): boolean {
    return this.message.type === MessageType.GAME;
  }

  get authorInitial(): string {
    return this.message.author.name.charAt(0).toUpperCase();
  }

  get authorName(): string {
    return this.message.author.name;
  }

  get messageContent(): string {
    return this.message.content;
  }

  get authorColor(): string {
    return this.message.author.color;
  }
}
