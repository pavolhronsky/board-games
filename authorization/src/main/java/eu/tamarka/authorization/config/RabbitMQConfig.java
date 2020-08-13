package eu.tamarka.authorization.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

  @Bean
  public FanoutExchange fanoutExchange() {
    return new FanoutExchange("users.fanout");
  }

  @Bean
  public Queue usersTicTacToeQueue() {
    return new Queue("users.tictactoe", true);
  }

  @Bean
  public Binding ticTacToeBinding() {
    return BindingBuilder.bind(usersTicTacToeQueue()).to(fanoutExchange());
  }

  @Bean
  public Queue usersTicketToRideSlovakiaQueue() {
    return new Queue("users.tickettorideslovakia", true);
  }

  @Bean
  public Binding ticketToRideSlovakiaBinding() {
    return BindingBuilder.bind(usersTicketToRideSlovakiaQueue()).to(fanoutExchange());
  }

  @Bean
  public Queue usersChatQueue() {
    return new Queue("users.chat", true);
  }

  @Bean
  public Binding usersBinding() {
    return BindingBuilder.bind(usersChatQueue()).to(fanoutExchange());
  }

  @Bean
  public Jackson2JsonMessageConverter converter() {
    return new Jackson2JsonMessageConverter();
  }

  @Bean
  public RabbitTemplate rabbitTemplate(final ConnectionFactory factory) {
    final RabbitTemplate template = new RabbitTemplate(factory);
    template.setMessageConverter(converter());
    return template;
  }
}