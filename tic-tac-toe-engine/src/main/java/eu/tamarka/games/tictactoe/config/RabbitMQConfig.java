package eu.tamarka.games.tictactoe.config;

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
  public Queue usersQueue() {
    return new Queue("users.tictactoe", true);
  }

  @Bean
  public Binding binding() {
    return BindingBuilder.bind(usersQueue()).to(fanoutExchange());
  }

  @Bean
  public Jackson2JsonMessageConverter converter() {
    return new Jackson2JsonMessageConverter();
  }

  @Bean
  public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
    final RabbitTemplate template = new RabbitTemplate(connectionFactory);
    template.setMessageConverter(converter());
    return template;
  }
}