package cuit.cn.interviewService.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String INTERVIEW_EXCHANGE = "interview.exchange";
    public static final String INTERVIEW_NOTIFICATION_QUEUE = "interview.notification.queue";
    public static final String INTERVIEW_NOTIFICATION_ROUTING_KEY = "interview.notification";

    @Bean
    public DirectExchange interviewExchange() {
        return new DirectExchange(INTERVIEW_EXCHANGE);
    }

    @Bean
    public Queue interviewNotificationQueue() {
        return QueueBuilder.durable(INTERVIEW_NOTIFICATION_QUEUE).build();
    }

    @Bean
    public Binding interviewNotificationBinding() {
        return BindingBuilder
            .bind(interviewNotificationQueue())
                .to(interviewExchange())
            .with(INTERVIEW_NOTIFICATION_ROUTING_KEY);
    }
    
    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
        return rabbitTemplate;
    }
    
    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
} 