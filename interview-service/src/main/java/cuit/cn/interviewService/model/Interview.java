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
    
    @Column(nullable = false)
    private Long jobId;
    
    @Column(nullable = false)
    private Long userId;
    
    @Column(nullable = false)
    private Long companyId;
    
    @Column(nullable = true)
    private Long applicationId;
    
    @Column(nullable = false)
    private LocalDateTime scheduleTime;
    
    @Column(nullable = false)
    private String interviewMethod;
    
    private String location;
    
    private String interviewer;
    
    @Column(columnDefinition = "TEXT")
    private String feedback;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InterviewStatus status;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(nullable = false)
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
} 