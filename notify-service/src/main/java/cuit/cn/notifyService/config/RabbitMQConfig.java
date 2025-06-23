package cuit.cn.notifyService.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.messaging.handler.annotation.support.MessageHandlerMethodFactory;

@Configuration
@ConditionalOnProperty(name = "spring.rabbitmq.enabled", havingValue = "true", matchIfMissing = true)
public class RabbitMQConfig implements RabbitListenerConfigurer {

    @Value("${rabbitmq.exchange.interview}")
    private String interviewExchange;

    @Value("${rabbitmq.queue.interview-notification}")
    private String interviewNotificationQueue;

    @Value("${rabbitmq.routing-key.interview-notification}")
    private String interviewNotificationRoutingKey;

    @Value("${rabbitmq.exchange.application}")
    private String applicationExchange;

    @Value("${rabbitmq.queue.application-notification}")
    private String applicationNotificationQueue;

    @Value("${rabbitmq.routing-key.application-notification}")
    private String applicationNotificationRoutingKey;

    @Bean
    public DirectExchange interviewExchange() {
        return new DirectExchange(interviewExchange);
    }

    @Bean
    public Queue interviewNotificationQueue() {
        return new Queue(interviewNotificationQueue);
    }

    @Bean
    public Binding interviewNotificationBinding() {
        return BindingBuilder.bind(interviewNotificationQueue())
                .to(interviewExchange())
                .with(interviewNotificationRoutingKey);
    }

    @Bean
    public DirectExchange applicationExchange() {
        return new DirectExchange(applicationExchange);
    }

    @Bean
    public Queue applicationNotificationQueue() {
        return new Queue(applicationNotificationQueue);
    }

    @Bean
    public Binding applicationNotificationBinding() {
        return BindingBuilder.bind(applicationNotificationQueue())
                .to(applicationExchange())
                .with(applicationNotificationRoutingKey);
    }

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
        registrar.setMessageHandlerMethodFactory(messageHandlerMethodFactory());
    }

    @Bean
    public MessageHandlerMethodFactory messageHandlerMethodFactory() {
        DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
        factory.setMessageConverter(consumerJackson2MessageConverter());
        return factory;
    }

    @Bean
    public MappingJackson2MessageConverter consumerJackson2MessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(objectMapper());
        return converter;
    }
    
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper;
    }
} 