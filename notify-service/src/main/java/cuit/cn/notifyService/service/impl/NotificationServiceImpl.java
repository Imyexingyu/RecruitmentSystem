package cuit.cn.notifyService.service.impl;

import cuit.cn.notifyService.dto.NotificationDTO;
import cuit.cn.notifyService.feign.UserServiceClient;
import cuit.cn.notifyService.model.Notification;
import cuit.cn.notifyService.repository.NotificationRepository;
import cuit.cn.notifyService.service.NotificationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {

    private static final Logger logger = Logger.getLogger(NotificationServiceImpl.class.getName());

    private final NotificationRepository notificationRepository;
    private final UserServiceClient userServiceClient;

    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository, UserServiceClient userServiceClient) {
        this.notificationRepository = notificationRepository;
        this.userServiceClient = userServiceClient;
    }

    @Override
    @Transactional
    public NotificationDTO createNotification(Long userId, String title, String content, String type) {
        // 参数验证
        if (userId == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        
        if (title == null || title.isEmpty()) {
            title = "系统通知"; // 使用默认标题
        }
        
        if (content == null || content.isEmpty()) {
            throw new IllegalArgumentException("通知内容不能为空");
        }
        
        // 与用户服务集成，验证用户是否存在
        ResponseEntity<Map<String, Object>> userResponse = userServiceClient.getUserById(userId);
        if (userResponse.getBody() != null && userResponse.getBody().containsKey("error")) {
            logger.warning("用户验证失败: " + userResponse.getBody().get("error"));
        }
        
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setType(type != null ? type : "SYSTEM"); // 默认为系统通知
        notification.setIsRead(false);
        notification.setCreatedAt(LocalDateTime.now());
        
        try {
        Notification savedNotification = notificationRepository.save(notification);
            logger.info("创建通知成功，ID: " + savedNotification.getId() + ", 用户ID: " + userId);
            
            // TODO: 与消息推送服务集成，实时推送通知给用户
            
        return convertToDTO(savedNotification);
        } catch (Exception e) {
            logger.severe("创建通知失败: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public Page<NotificationDTO> getUserNotifications(Long userId, Pageable pageable) {
        if (userId == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        
        // 与用户服务集成，验证用户是否存在
        ResponseEntity<Map<String, Object>> userResponse = userServiceClient.getUserById(userId);
        if (userResponse.getBody() != null && userResponse.getBody().containsKey("error")) {
            logger.warning("用户验证失败: " + userResponse.getBody().get("error"));
        }
        
        Page<Notification> notifications = notificationRepository.findByUserId(userId, pageable);
        return notifications.map(this::convertToDTO);
    }

    @Override
    public List<NotificationDTO> getUnreadNotifications(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        
        // 与用户服务集成，验证用户是否存在
        ResponseEntity<Map<String, Object>> userResponse = userServiceClient.getUserById(userId);
        if (userResponse.getBody() != null && userResponse.getBody().containsKey("error")) {
            logger.warning("用户验证失败: " + userResponse.getBody().get("error"));
        }
        
        List<Notification> notifications = notificationRepository.findByUserIdAndIsRead(userId, false);
        return notifications.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public long countUnreadNotifications(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        
        // 与用户服务集成，验证用户是否存在
        ResponseEntity<Map<String, Object>> userResponse = userServiceClient.getUserById(userId);
        if (userResponse.getBody() != null && userResponse.getBody().containsKey("error")) {
            logger.warning("用户验证失败: " + userResponse.getBody().get("error"));
        }
        
        return notificationRepository.countByUserIdAndIsRead(userId, false);
    }

    @Override
    @Transactional
    public void markAsRead(Long notificationId) {
        if (notificationId == null) {
            throw new IllegalArgumentException("通知ID不能为空");
        }
        
        try {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new EntityNotFoundException("通知不存在，ID: " + notificationId));
        
        notification.setIsRead(true);
        notificationRepository.save(notification);
            logger.info("标记通知为已读，ID: " + notificationId);
        } catch (EntityNotFoundException e) {
            logger.warning(e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.severe("标记通知已读失败: " + e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional
    public void markAllAsRead(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        
        // 与用户服务集成，验证用户是否存在
        ResponseEntity<Map<String, Object>> userResponse = userServiceClient.getUserById(userId);
        if (userResponse.getBody() != null && userResponse.getBody().containsKey("error")) {
            logger.warning("用户验证失败: " + userResponse.getBody().get("error"));
        }
        
        try {
        List<Notification> unreadNotifications = notificationRepository.findByUserIdAndIsRead(userId, false);
            if (unreadNotifications.isEmpty()) {
                logger.info("用户没有未读通知，用户ID: " + userId);
                return;
            }
            
        unreadNotifications.forEach(notification -> notification.setIsRead(true));
        notificationRepository.saveAll(unreadNotifications);
            logger.info("标记所有通知为已读，用户ID: " + userId + ", 数量: " + unreadNotifications.size());
        } catch (Exception e) {
            logger.severe("标记所有通知为已读失败: " + e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional
    public void deleteNotification(Long notificationId) {
        if (notificationId == null) {
            throw new IllegalArgumentException("通知ID不能为空");
        }
        
        try {
            // 验证通知是否存在
            if (!notificationRepository.existsById(notificationId)) {
                throw new EntityNotFoundException("通知不存在，ID: " + notificationId);
            }
            
        notificationRepository.deleteById(notificationId);
            logger.info("删除通知成功，ID: " + notificationId);
        } catch (EntityNotFoundException e) {
            logger.warning(e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.severe("删除通知失败: " + e.getMessage());
            throw e;
        }
    }
    
    @Override
    @Transactional
    public Notification saveNotification(Notification notification) {
        if (notification == null) {
            throw new IllegalArgumentException("通知对象不能为空");
        }
        
        if (notification.getUserId() == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        
        // 与用户服务集成，验证用户是否存在
        ResponseEntity<Map<String, Object>> userResponse = userServiceClient.getUserById(notification.getUserId());
        if (userResponse.getBody() != null && userResponse.getBody().containsKey("error")) {
            logger.warning("用户验证失败: " + userResponse.getBody().get("error"));
        }
        
        try {
            // 确保创建时间存在
            if (notification.getCreatedAt() == null) {
                notification.setCreatedAt(LocalDateTime.now());
            }
            
        return notificationRepository.save(notification);
        } catch (Exception e) {
            logger.severe("保存通知失败: " + e.getMessage());
            throw e;
        }
    }
    
    private NotificationDTO convertToDTO(Notification notification) {
        NotificationDTO dto = new NotificationDTO();
        BeanUtils.copyProperties(notification, dto);
        return dto;
    }
} 