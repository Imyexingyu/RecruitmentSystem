package cuit.cn.interviewService.service;

import cuit.cn.interviewService.dto.InterviewDTO;
import cuit.cn.interviewService.dto.InterviewRequest;
import cuit.cn.interviewService.dto.InterviewUpdateRequest;
import cuit.cn.interviewService.model.Interview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InterviewService {
    
    InterviewDTO scheduleInterview(InterviewRequest request);
    
    InterviewDTO getInterview(Long id);
    
    Page<InterviewDTO> getInterviewsByApplicationId(Long applicationId, Pageable pageable);
    
    InterviewDTO updateInterview(Long id, InterviewUpdateRequest request);
    
    void cancelInterview(Long id);
    
    InterviewDTO updateInterviewStatus(Long id, String status);
    
    InterviewDTO addInterviewFeedback(Long id, String feedback);

    Interview createInterview(InterviewDTO interviewDTO);
    
    Interview updateInterview(Long id, InterviewDTO interviewDTO);
    
    void deleteInterview(Long id);
    
    Interview getInterviewById(Long id);
    
    List<Interview> getInterviewsByJobId(Long jobId);
    
    List<Interview> getInterviewsByUserId(Long userId);
    
    void sendInterviewNotification(Interview interview, String notificationType);
} 