package cuit.cn.interviewService.config;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import static cuit.cn.interviewService.config.RabbitMQConfig.*;

@Component
public class RabbitTestSender implements CommandLineRunner {

    private final RabbitTemplate rabbitTemplate;

    public RabbitTestSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void run(String... args) {
        try {
            String message = "这是一条面试通知测试消息";
            rabbitTemplate.convertAndSend(INTERVIEW_EXCHANGE, INTERVIEW_NOTIFICATION_ROUTING_KEY, message);
            System.out.println("✅ 成功发送测试消息到 RabbitMQ！");
        } catch (Exception e) {
            System.err.println("❌ RabbitMQ 消息发送失败：" + e.getMessage());
            e.printStackTrace();
        }
    }
}
