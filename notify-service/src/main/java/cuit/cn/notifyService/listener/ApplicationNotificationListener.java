package cuit.cn.notifyService.listener;

import cuit.cn.notifyService.dto.ApplicationNotificationDTO;
import cuit.cn.notifyService.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
@ConditionalOnProperty(name = "spring.rabbitmq.enabled", havingValue = "true", matchIfMissing = true)
public class ApplicationNotificationListener {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationNotificationListener.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
    private final NotificationService notificationService;

    @Autowired
    public ApplicationNotificationListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @RabbitListener(queues = "${rabbitmq.queue.application-notification}")
    public void handleApplicationNotification(ApplicationNotificationDTO notification) {
        logger.info("接收到简历投递通知消息: {}", notification);
        
        try {
            String title = generateNotificationTitle(notification);
            String content = generateNotificationContent(notification);
            
            // 给求职者发送通知
            if (notification.getUserId() != null) {
                notificationService.createNotification(
                        notification.getUserId(),
                        title,
                        content,
                        "简历投递通知"
                );
                logger.info("已为求职者创建简历投递通知, userId: {}", notification.getUserId());
            }
            
            // 给企业发送通知
            if (notification.getCompanyId() != null) {
                notificationService.createNotification(
                        notification.getCompanyId(),
                        title,
                        content,
                        "简历投递通知"
                );
                logger.info("已为企业创建简历投递通知, companyId: {}", notification.getCompanyId());
            }
            
        } catch (Exception e) {
            logger.error("处理简历投递通知消息失败: {}", e.getMessage());
        }
    }
    
    private String generateNotificationTitle(ApplicationNotificationDTO notification) {
        switch (notification.getNotificationType()) {
            case "新简历投递":
                return "您有一份新的简历投递";
            case "简历状态变更":
                return "您的简历投递状态已更新";
            default:
                return "简历投递通知";
        }
    }
    
    private String generateNotificationContent(ApplicationNotificationDTO notification) {
        StringBuilder content = new StringBuilder();
        
        switch (notification.getNotificationType()) {
            case "新简历投递":
                if (notification.getUserId() != null) {
                    content.append("您已成功投递简历到职位：").append(notification.getJobTitle()).append("\n");
                } else if (notification.getCompanyId() != null) {
                    content.append("您收到一份新的简历投递，职位：").append(notification.getJobTitle()).append("\n");
                }
                break;
            case "简历状态变更":
                if (notification.getUserId() != null) {
                    content.append("您投递的简历状态已更新，职位：").append(notification.getJobTitle()).append("\n");
                } else if (notification.getCompanyId() != null) {
                    content.append("您已更新求职者简历状态，职位：").append(notification.getJobTitle()).append("\n");
                }
                break;
            default:
                content.append("简历投递通知：\n");
                break;
        }
        
        content.append("职位：").append(notification.getJobTitle()).append("\n");
        content.append("简历：").append(notification.getResumeTitle()).append("\n");
        content.append("当前状态：").append(notification.getStatus()).append("\n");
        content.append("更新时间：").append(notification.getNotificationTime().format(formatter)).append("\n");
        
        return content.toString();
    }
} 