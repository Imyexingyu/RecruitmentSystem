package cuit.cn.interviewService.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterviewUpdateRequest {
    @Future(message = "面试时间必须是将来的时间")
    private LocalDateTime scheduleTime;
    
    @Pattern(regexp = "(?i)^(ONLINE|OFFLINE)$", message = "面试方式必须是 ONLINE 或 OFFLINE")
    private String interviewMethod;
    
    private String location;
    private String interviewer;
    private String feedback;
} 