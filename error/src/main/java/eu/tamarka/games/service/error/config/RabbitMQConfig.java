package eu.tamarka.games.service.error.config;

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
  public FanoutExchange usersExchange() {
    return new FanoutExchange("users.fanout");
  }

  @Bean
  public Queue usersQueue() {
    return new Queue("users.error", true);
  }

  @Bean
  public Binding usersBinding() {
    return BindingBuilder.bind(usersQueue()).to(usersExchange());
  }

  @Bean
  public Queue errorQueue() {
    return new Queue("general.error");
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