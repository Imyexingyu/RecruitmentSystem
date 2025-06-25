package cuit.cn.notifyService.controller;

import cuit.cn.notifyService.dto.NotificationDTO;
import cuit.cn.notifyService.dto.NotificationResponse;
import cuit.cn.notifyService.service.NotificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@Api(tags = "通知管理接口")
public class NotificationController {
    
    private final NotificationService notificationService;
    
    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }
    
    @GetMapping("/user/{userId}")
    @ApiOperation("获取用户的所有通知")
    public ResponseEntity<NotificationResponse<Page<NotificationDTO>>> getUserNotifications(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            Page<NotificationDTO> notifications = notificationService.getUserNotifications(userId, pageable);
            return ResponseEntity.ok(NotificationResponse.success(notifications));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(NotificationResponse.fail(e.getMessage()));
        }
    }
    
    @GetMapping("/user/{userId}/unread")
    @ApiOperation("获取用户的未读通知")
    public ResponseEntity<NotificationResponse<List<NotificationDTO>>> getUnreadNotifications(@PathVariable Long userId) {
        try {
            List<NotificationDTO> notifications = notificationService.getUnreadNotifications(userId);
            return ResponseEntity.ok(NotificationResponse.success(notifications));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(NotificationResponse.fail(e.getMessage()));
        }
    }
    
    @GetMapping("/user/{userId}/unread/count")
    @ApiOperation("获取用户的未读通知数量")
    public ResponseEntity<NotificationResponse<Long>> countUnreadNotifications(@PathVariable Long userId) {
        try {
            long count = notificationService.countUnreadNotifications(userId);
            return ResponseEntity.ok(NotificationResponse.success(count));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(NotificationResponse.fail(e.getMessage()));
        }
    }
    
    @PutMapping("/{notificationId}/read")
    @ApiOperation("标记通知为已读")
    public ResponseEntity<NotificationResponse<Void>> markAsRead(@PathVariable Long notificationId) {
        try {
            notificationService.markAsRead(notificationId);
            return ResponseEntity.ok(NotificationResponse.success("通知已标记为已读", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(NotificationResponse.fail(e.getMessage()));
        }
    }
    
    @PutMapping("/user/{userId}/read-all")
    @ApiOperation("标记所有通知为已读")
    public ResponseEntity<NotificationResponse<Void>> markAllAsRead(@PathVariable Long userId) {
        try {
            notificationService.markAllAsRead(userId);
            return ResponseEntity.ok(NotificationResponse.success("所有通知已标记为已读", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(NotificationResponse.fail(e.getMessage()));
        }
    }
    
    @DeleteMapping("/{notificationId}")
    @ApiOperation("删除通知")
    public ResponseEntity<NotificationResponse<Void>> deleteNotification(@PathVariable Long notificationId) {
        try {
            notificationService.deleteNotification(notificationId);
            return ResponseEntity.ok(NotificationResponse.success("通知已删除", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(NotificationResponse.fail(e.getMessage()));
        }
    }
} 