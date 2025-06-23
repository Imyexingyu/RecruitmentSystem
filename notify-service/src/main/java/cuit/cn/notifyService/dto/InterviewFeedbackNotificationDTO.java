package cuit.cn.notifyService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterviewFeedbackNotificationDTO {
    
    private Long interviewId;
    private Long userId;
    private Long companyId;
    private String feedback;
    private String notificationType;
    private String title;
    private String content;
    private String type;
    private LocalDateTime createdAt;
} 