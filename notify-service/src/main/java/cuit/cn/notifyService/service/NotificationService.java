package cuit.cn.notifyService.service;

import cuit.cn.notifyService.dto.NotificationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import cuit.cn.notifyService.model.Notification;

import java.util.List;

public interface NotificationService {
    
    NotificationDTO createNotification(Long userId, String title, String content, String type);
    
    Page<NotificationDTO> getUserNotifications(Long userId, Pageable pageable);
    
    List<NotificationDTO> getUnreadNotifications(Long userId);
    
    long countUnreadNotifications(Long userId);
    
    void markAsRead(Long notificationId);
    
    void markAllAsRead(Long userId);
    
    void deleteNotification(Long notificationId);

    Notification saveNotification(Notification notification);
} 