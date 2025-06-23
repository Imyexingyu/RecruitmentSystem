package cuit.cn.interviewService.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterviewDTO {
    private Long id;
    
    @NotNull
    private Long jobId;
    
    @NotNull
    private Long userId;
    
    @NotNull
    private Long companyId;
    
    @NotNull
    private LocalDateTime scheduleTime;
    
    @NotNull
    private String interviewMethod;
    
    private String location;
    
    private String interviewer;
    
    private String feedback;
} 