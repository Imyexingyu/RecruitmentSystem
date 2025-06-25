package cuit.cn.interviewService.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Future;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewRequest {
    @NotNull(message = "职位ID不能为空")
    private Long jobId;
    
    @NotNull(message = "用户ID不能为空")
    private Long userId;
    
    @NotNull(message = "面试时间不能为空")
    private LocalDateTime scheduledTime;
    
    @NotNull(message = "面试地点不能为空")
    private String location;
    
    @NotNull(message = "面试方式不能为空")
    @Pattern(regexp = "^(ONLINE|OFFLINE)$", message = "面试方式必须是 ONLINE 或 OFFLINE")
    private String interviewType;
    
    @NotNull(message = "面试官不能为空")
    private String interviewer;
    
    @NotNull(message = "面试状态不能为空")
    @Pattern(regexp = "^(SCHEDULED|IN_PROGRESS|COMPLETED|CANCELLED|NO_SHOW|PENDING)$", message = "面试状态必须是：SCHEDULED、IN_PROGRESS、COMPLETED、CANCELLED、NO_SHOW或PENDING")
    private String status;
    
    // 为了兼容性添加的方法
    public Long getCandidateId() {
        return this.userId;
    }
    
    public void setCandidateId(Long candidateId) {
        this.userId = candidateId;
    }
} 