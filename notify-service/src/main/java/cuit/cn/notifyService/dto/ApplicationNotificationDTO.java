package cuit.cn.notifyService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationNotificationDTO implements Serializable {
    private Long applicationId;
    private Long userId;  // 求职者ID
    private Long companyId;  // 企业ID
    private Long jobId;
    private String jobTitle;
    private String resumeTitle;
    private String status;  // 投递状态：已投递/已查看/已通过筛选/已拒绝
    private String notificationType;  // 通知类型：新简历投递/简历状态变更
    private LocalDateTime notificationTime;
} 