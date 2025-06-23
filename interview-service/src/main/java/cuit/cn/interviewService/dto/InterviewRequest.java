package cuit.cn.interviewService.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Future;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterviewRequest {
    @NotNull(message = "职位ID不能为空")
    private Long jobId;
    
    @NotNull(message = "用户ID不能为空")
    private Long userId;
    
    @NotNull(message = "公司ID不能为空")
    private Long companyId;
    
    private Long applicationId;
    
    @NotNull(message = "面试时间不能为空")
    @Future(message = "面试时间必须是将来的时间")
    private LocalDateTime scheduleTime;
    
    @NotNull(message = "面试方式不能为空")
    @Pattern(regexp = "(?i)^(ONLINE|OFFLINE)$", message = "面试方式必须是 ONLINE 或 OFFLINE")
    private String interviewMethod;
    
    private String location;
    
    private String interviewer;
    
    private String status;
} 