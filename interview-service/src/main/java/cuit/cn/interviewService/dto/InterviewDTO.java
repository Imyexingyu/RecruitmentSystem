package cuit.cn.interviewService.dto;

import cuit.cn.interviewService.model.InterviewStatus;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewDTO {
    private Long id;
    private Long jobId;
    private Long userId;
    private LocalDateTime scheduledTime;
    private String location;
    private String interviewType;
    private String status;
    private String feedback;
    private Long createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // 为了兼容性添加的方法
    public Long getCandidateId() {
        return this.userId;
    }
    
    public void setCandidateId(Long candidateId) {
        this.userId = candidateId;
    }
    
    public LocalDateTime getScheduleTime() {
        return this.scheduledTime;
    }
    
    public void setScheduleTime(LocalDateTime scheduleTime) {
        this.scheduledTime = scheduleTime;
    }
    
    public String getInterviewMethod() {
        return this.interviewType;
    }
    
    public void setInterviewMethod(String interviewMethod) {
        this.interviewType = interviewMethod;
    }
} 