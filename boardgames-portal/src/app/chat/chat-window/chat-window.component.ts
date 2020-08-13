import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {RxStompService} from '@stomp/ng2-stompjs';
import {OidcSecurityService} from 'angular-auth-oidc-client';
import {Message} from '@stomp/stompjs';
import {ChatMessage} from '../chat-message';
import {Subscription} from 'rxjs';
import {environment} from 'src/environments/environment';
import {MessageType} from '../message-type.enum';
import {FormBuilder, FormControl, Validators} from '@angular/forms';
import {LogManager} from '../../logging/log-manager';

@Component({
  selector: 'app-chat-window',
  templateUrl: './chat-window.component.html',
  styleUrls: ['./chat-window.component.css'],
  providers: [RxStompService]
})
export class ChatWindowComponent implements OnInit, OnDestroy {

  private log = LogManager.getLogger('ChatWindowComponent');

  private chatUserSubscription: Subscription;
  private chatSubscription: Subscription;

  @Input() private gameName: string;
  @Input() private gameId: string;
  @Input() private userColorsMap: Map<string, string>;
  @Input() private userData: any;

  public newMessageControl: FormControl;
  private messages: ChatMessage[] = [];

  constructor(private oidcSecurityService: OidcSecurityService,
              private chatService: RxStompService,
              private fb: FormBuilder) {
    //
  }

  ngOnInit(): void {
    this.chatService.configure({
      brokerURL: environment.chatUrlWs,
      connectHeaders: {
        login: '',
        passcode: '',
        userId: this.userData.sub
      },
      heartbeatIncoming: 0,
      heartbeatOutgoing: 20000,
      reconnectDelay: 1000,
      debug: str => {
        this.log.debug(str);
      }
    });

    this.chatService.activate();

    this.newMessageControl = this.fb.control('', Validators.required);

    this.chatUserSubscription = this.chatService.watch(`/user/chat/games/${this.gameName}/${this.gameId}`).subscribe(
      (msg: Message) => {
        const messagesHistory = JSON.parse(msg.body) as ChatMessage[];
        this.log.debug('Messages history: {}.', messagesHistory);
        messagesHistory.forEach((m: ChatMessage) => {
            this.handleNewMessage(m);
          }
        );
      }
    );
    this.chatSubscription = this.chatService.watch(`/chat/games/${this.gameName}/${this.gameId}`).subscribe(
      (msg: Message) => {
        const newMessage = JSON.parse(msg.body) as ChatMessage;
        this.log.debug('New message received: {}.', newMessage);
        this.handleNewMessage(newMessage);
      }
    );
  }

  ngOnDestroy(): void {
    this.chatUserSubscription.unsubscribe();
    this.chatSubscription.unsubscribe();
    this.chatService.deactivate();
  }

  get getMessages(): ChatMessage[] {
    return this.messages;
  }

  private handleNewMessage(message: ChatMessage) {
    switch (message.type) {
      case MessageType.CONNECTION: {
        break;
      }
      case MessageType.USER: {
        message.author.color = this.userColorsMap.get(message.author.id);
        break;
      }
      default: {
        //
      }
    }
    this.messages.push(message);
    this.scrollToTheBottom();
  }

  private scrollToTheBottom() {
    this.log.debug('Height: ', document.getElementById('chat-messages').scrollHeight);
    this.log.debug('Top: ', document.getElementById('chat-messages').scrollTop);

    const val = document.getElementById('chat-messages').scrollHeight;
    document.getElementById('chat-messages').scrollTop = val + 30;
  }

  showHide() {
    document.getElementById('chat').classList.toggle('hidden');
  }

  send() {
    if (this.newMessageControl.valid) {
      this.chatService.publish({destination: `/command/send/${this.gameName}/${this.gameId}`, body: this.newMessageControl.value});
      this.newMessageControl.setValue('');
    }
  }
}
