package cuit.cn.notifyService.listener;

import cuit.cn.notifyService.model.Notification;
import cuit.cn.notifyService.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

@Component
@Slf4j
@ConditionalOnProperty(name = "spring.rabbitmq.enabled", havingValue = "true", matchIfMissing = true)
public class InterviewNotificationListener {

    private final NotificationService notificationService;

    @Autowired
    public InterviewNotificationListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @RabbitListener(queues = "${rabbitmq.queue.interview-notification}")
    public void handleInterviewNotification(Map<String, Object> notificationMap) {
        log.info("收到面试通知消息: {}", notificationMap);
        
        try {
            // 安全地获取必要字段
            Long userId = getLongValue(notificationMap, "userId");
            String title = getStringValue(notificationMap, "title");
            String content = getStringValue(notificationMap, "content");
            String type = getStringValue(notificationMap, "type", "INTERVIEW"); // 默认为INTERVIEW类型
            Long interviewId = getLongValue(notificationMap, "interviewId");
            String notificationType = getStringValue(notificationMap, "notificationType");
            
            log.info("解析消息: userId={}, title={}, type={}, interviewId={}", userId, title, type, interviewId);
            
            // 验证必要字段
            if (userId == null) {
                log.error("通知消息缺少userId字段");
                throw new IllegalArgumentException("通知消息缺少userId字段");
            }
            
            if (title == null || title.isEmpty()) {
                title = "面试通知"; // 使用默认标题
                log.warn("通知消息缺少title字段，使用默认值");
            }
            
            if (content == null || content.isEmpty()) {
                // 尝试从其他字段构建内容
                content = buildContentFromNotification(notificationMap);
                log.warn("通知消息缺少content字段，从其他字段构建内容");
            }
            
            // 创建通知对象
            Notification notification = new Notification();
            notification.setUserId(userId);
            notification.setTitle(title);
            notification.setContent(content);
            notification.setType(type);
            notification.setIsRead(false);
            notification.setCreatedAt(LocalDateTime.now());
            notification.setNotificationSource("INTERVIEW_SERVICE");
            notification.setReferenceId(interviewId);
            
            // 保存通知记录
        notificationService.saveNotification(notification);
            log.info("面试通知处理成功，已保存通知记录，用户ID: {}, 面试ID: {}", userId, interviewId);
            
        } catch (IllegalArgumentException e) {
            // 消息格式错误，拒绝消息并且不重新入队
            log.error("通知消息格式错误: {}", e.getMessage());
            throw new AmqpRejectAndDontRequeueException("消息格式错误: " + e.getMessage(), e);
        } catch (Exception e) {
            // 其他未预期的异常，记录错误但允许消息重新入队
            log.error("处理通知失败: {}", e.getMessage(), e);
            // 重新抛出异常，让Spring AMQP处理重试逻辑
            throw e;
        }
    }
    
    /**
     * 安全地从Map中获取Long类型值
     */
    private Long getLongValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) {
            return null;
        }
        
        if (value instanceof Number) {
            return ((Number) value).longValue();
        } else if (value instanceof String) {
            try {
                return Long.parseLong((String) value);
            } catch (NumberFormatException e) {
                log.warn("无法将字段{}的值{}转换为Long类型", key, value);
                return null;
            }
        }
        
        log.warn("字段{}的值{}不是有效的Long类型", key, value);
        return null;
    }
    
    /**
     * 安全地从Map中获取String类型值
     */
    private String getStringValue(Map<String, Object> map, String key) {
        return getStringValue(map, key, null);
    }
    
    /**
     * 安全地从Map中获取String类型值，如果不存在则返回默认值
     */
    private String getStringValue(Map<String, Object> map, String key, String defaultValue) {
        Object value = map.get(key);
        if (value == null) {
            return defaultValue;
        }
        
        return value.toString();
    }
    
    /**
     * 从通知消息中构建内容
     */
    private String buildContentFromNotification(Map<String, Object> notificationMap) {
        StringBuilder content = new StringBuilder("面试通知：\n");
        
        // 尝试获取关键信息
        Object scheduleTime = notificationMap.get("scheduleTime");
        Object interviewMethod = notificationMap.get("interviewMethod");
        Object location = notificationMap.get("location");
        Object interviewer = notificationMap.get("interviewer");
        Object notificationType = notificationMap.get("notificationType");
        
        // 根据通知类型添加前缀
        if (notificationType != null) {
            switch (notificationType.toString()) {
                case "新面试安排":
                    content = new StringBuilder("您有一个新的面试安排，详情如下：\n");
                    break;
                case "面试变更":
                    content = new StringBuilder("您的面试安排已变更，最新详情如下：\n");
                    break;
                case "面试取消":
                    content = new StringBuilder("您的面试已被取消，原面试详情如下：\n");
                    break;
                case "面试状态更新":
                    content = new StringBuilder("您的面试状态已更新，详情如下：\n");
                    break;
            }
        }
        
        // 添加面试详情
        if (scheduleTime != null) {
            content.append("面试时间：").append(scheduleTime).append("\n");
        }
        
        if (interviewMethod != null) {
            content.append("面试方式：").append(interviewMethod).append("\n");
        }
        
        if (location != null) {
            content.append("面试地点：").append(location).append("\n");
        }
        
        if (interviewer != null) {
            content.append("面试官：").append(interviewer).append("\n");
        }
        
        return content.toString();
    }
} 