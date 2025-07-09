package cuit.cn.interviewService.service;

import cuit.cn.interviewService.dto.InterviewDTO;
import cuit.cn.interviewService.dto.InterviewRequest;
import cuit.cn.interviewService.model.InterviewStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InterviewService {
    InterviewDTO createInterview(InterviewRequest request, Long currentUserId);
    
    List<InterviewDTO> getInterviewsByCandidate(Long candidateId);
    
    List<InterviewDTO> getAllInterviews();
    
    InterviewDTO updateInterviewStatus(Long id, String status);
    
    InterviewDTO addInterviewFeedback(Long id, String feedback);

    InterviewDTO getInterview(Long id);

    Page<InterviewDTO> getInterviewsByJobId(Long jobId, Pageable pageable);
} 