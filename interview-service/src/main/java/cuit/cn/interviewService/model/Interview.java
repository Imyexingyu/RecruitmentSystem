package cuit.cn.interviewService.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "interviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Interview {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "job_id", nullable = false)
    private Long jobId;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "scheduled_time", nullable = false)
    private LocalDateTime scheduledTime;
    
    @Column(nullable = false)
    private String location;
    
    @Column(name = "interview_type", nullable = false)
    private String interviewType;
    
    @Column(nullable = false)
    private String status;
    
    @Column(name = "feedback")
    private String feedback;
    
    @Column(name = "interviewer", nullable = false)
    private String interviewer;
    
    @Column(name = "created_by", nullable = false)
    private Long createdBy;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    // 兼容性方法 - candidateId 实际上就是 userId
    @Transient
    public Long getCandidateId() {
        return this.userId;
    }
    
    public void setCandidateId(Long candidateId) {
        this.userId = candidateId;
    }
    
    // 兼容性方法 - scheduleTime 实际上就是 scheduledTime
    @Transient
    public LocalDateTime getScheduleTime() {
        return this.scheduledTime;
    }
    
    public void setScheduleTime(LocalDateTime scheduleTime) {
        this.scheduledTime = scheduleTime;
    }
    
    // 兼容性方法 - interviewMethod 实际上就是 interviewType
    @Transient
    public String getInterviewMethod() {
        return this.interviewType;
    }
    
    public void setInterviewMethod(String interviewMethod) {
        this.interviewType = interviewMethod;
    }
    
    public String getInterviewer() {
        return this.interviewer;
    }
} 