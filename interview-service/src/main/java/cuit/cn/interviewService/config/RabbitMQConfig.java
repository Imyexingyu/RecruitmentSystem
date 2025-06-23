package cuit.cn.interviewService.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "spring.rabbitmq.enabled", havingValue = "true", matchIfMissing = true)
public class RabbitMQConfig {

    @Value("${rabbitmq.exchange.interview}")
    private String interviewExchange;

    @Value("${rabbitmq.queue.interview-notification}")
    private String interviewNotificationQueue;

    @Value("${rabbitmq.routing-key.interview-notification}")
    private String interviewNotificationRoutingKey;

    @Bean
    public DirectExchange interviewExchange() {
        return new DirectExchange(interviewExchange, true, false);
    }

    @Bean
    public Queue interviewNotificationQueue() {
        return new Queue(interviewNotificationQueue, true);
    }

    @Bean
    public Binding interviewNotificationBinding() {
        return BindingBuilder.bind(interviewNotificationQueue())
                .to(interviewExchange())
                .with(interviewNotificationRoutingKey);
    }
    
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper;
    }
    
    @Bean
    public MessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }
    
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter jsonMessageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter);
        
        // 设置消息确认回调
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (!ack) {
                System.err.println("消息发送失败: " + cause);
            }
        });
        
        // 设置消息返回回调
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            System.err.println("消息被退回: " + message + 
                    ", replyCode: " + replyCode + 
                    ", replyText: " + replyText + 
                    ", exchange: " + exchange + 
                    ", routingKey: " + routingKey);
        });
        
        // 设置强制消息返回
        rabbitTemplate.setMandatory(true);
        
        return rabbitTemplate;
    }
} 