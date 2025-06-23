package cuit.cn.interviewService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterviewNotificationDTO implements Serializable {
    private Long interviewId;
    private Long applicationId;
    private Long userId;  // 求职者ID
    private Long companyId;  // 企业ID
    private String interviewMethod;
    private LocalDateTime scheduleTime;
    private String location;
    private String interviewer;
    private String notificationType;  // 通知类型：新面试安排/面试变更/面试取消
    private LocalDateTime notificationTime;
} 