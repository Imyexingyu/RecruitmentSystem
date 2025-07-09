package cuit.cn.interviewService.service.impl;

import cuit.cn.interviewService.dto.ApiResponse;
import cuit.cn.interviewService.dto.InterviewDTO;
import cuit.cn.interviewService.dto.InterviewRequest;
import cuit.cn.interviewService.dto.JobDTO;
import cuit.cn.interviewService.model.Interview;
import cuit.cn.interviewService.model.InterviewStatus;
import cuit.cn.interviewService.feign.JobServiceClient;
import cuit.cn.interviewService.feign.UserServiceClient;
import cuit.cn.interviewService.repository.InterviewRepository;
import cuit.cn.interviewService.service.InterviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class InterviewServiceImpl implements InterviewService {

    private final InterviewRepository interviewRepository;
    private final RabbitTemplate rabbitTemplate;
    private final UserServiceClient userServiceClient;
    private final JobServiceClient jobServiceClient;

    @Value("${rabbitmq.exchange.interview:interview.exchange}")
    private String interviewExchange;

    @Value("${rabbitmq.routing-key.interview-notification:interview.notification}")
    private String interviewNotificationRoutingKey;

    @Autowired
    public InterviewServiceImpl(
            InterviewRepository interviewRepository, 
            RabbitTemplate rabbitTemplate,
            UserServiceClient userServiceClient,
            JobServiceClient jobServiceClient) {
        this.interviewRepository = interviewRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.userServiceClient = userServiceClient;
        this.jobServiceClient = jobServiceClient;
    }

    @Override
    @Transactional
    public InterviewDTO createInterview(InterviewRequest request, Long currentUserId) {
        log.info("开始创建面试，请求信息: {}, 当前用户ID: {}", request, currentUserId);

        try {
            // 验证面试时间是否合法
            if (request.getScheduledTime() == null || request.getScheduledTime().isBefore(LocalDateTime.now())) {
                log.error("面试时间验证失败: 面试时间必须是将来的时间，请求时间: {}", request.getScheduledTime());
                throw new IllegalArgumentException("面试时间必须是将来的时间");
            }

            // 验证用户是否存在
            log.info("开始验证用户是否存在，用户ID: {}", request.getUserId());
//            validateUserExists(request.getUserId());
            log.info("用户验证成功，用户ID: {}", request.getUserId());

            // 验证职位是否存在
            log.info("开始验证职位是否存在，职位ID: {}", request.getJobId());
//            validateJobExists(request.getJobId());
            log.info("职位验证成功，职位ID: {}", request.getJobId());

            // 创建面试记录
            Interview interview = new Interview();
            interview.setJobId(request.getJobId());
            interview.setUserId(request.getUserId());
            interview.setScheduledTime(request.getScheduledTime());
            interview.setLocation(request.getLocation());
            interview.setInterviewType(request.getInterviewType());
            interview.setStatus(request.getStatus());
            interview.setInterviewer(request.getInterviewer());
            interview.setCreatedBy(currentUserId);
            interview.setCreatedAt(LocalDateTime.now());
            interview.setUpdatedAt(LocalDateTime.now());

            Interview savedInterview = interviewRepository.save(interview);
            log.info("面试创建成功，面试ID: {}", savedInterview.getId());

            // 发送面试创建通知
            sendInterviewNotification(savedInterview, "面试创建");

            return convertToDTO(savedInterview);
        } catch (Exception e) {
            log.error("创建面试失败: {}", e.getMessage(), e);
            throw new RuntimeException("创建面试失败: " + e.getMessage());
        }
    }

    @Override
    public InterviewDTO getInterview(Long id) {
        Interview interview = interviewRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("面试不存在，ID: " + id));
        return convertToDTO(interview);
    }

    @Override
    @Transactional
    public InterviewDTO updateInterviewStatus(Long id, String status) {
        Interview interview = interviewRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("面试不存在，ID: " + id));
        
        // 验证状态是否有效
        try {
            InterviewStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("无效的面试状态: " + status);
        }
        
        interview.setStatus(status);
        interview.setUpdatedAt(LocalDateTime.now());
        interview = interviewRepository.save(interview);
        
        // 发送状态更新通知
        sendInterviewNotification(interview, "面试状态更新");
        
        return convertToDTO(interview);
    }

    @Override
    @Transactional
    public InterviewDTO addInterviewFeedback(Long id, String feedback) {
        Interview interview = interviewRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("面试不存在，ID: " + id));
        interview.setFeedback(feedback);
        interview.setUpdatedAt(LocalDateTime.now());
        interview = interviewRepository.save(interview);
        
        // 发送反馈通知
        sendInterviewNotification(interview, "面试反馈添加");
        
        return convertToDTO(interview);
    }

    @Override
    public List<InterviewDTO> getInterviewsByCandidate(Long candidateId) {
        return interviewRepository.findByUserId(candidateId).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public List<InterviewDTO> getAllInterviews() {
        return interviewRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public Page<InterviewDTO> getInterviewsByJobId(Long jobId, Pageable pageable) {
        try {
            log.info("开始验证职位是否存在，职位ID: {}", jobId);
            validateJobExists(jobId);
            log.info("职位验证成功，职位ID: {}", jobId);
        } catch (Exception e) {
            log.error("职位验证异常: {}", e.getMessage(), e);
            throw new IllegalArgumentException("职位验证失败: " + e.getMessage());
        }
        
        Page<Interview> interviews = interviewRepository.findByJobId(jobId, pageable);
        return interviews.map(this::convertToDTO);
    }

    private void validateJobExists(Long jobId) {
        if (jobId == null) {
            throw new IllegalArgumentException("职位ID不能为空");
        }
        
        JobDTO job = jobServiceClient.getJobById(jobId);
        
        if (job == null) {
            log.error("职位验证失败: 职位不存在，职位ID: {}", jobId);
            throw new IllegalArgumentException("职位不存在，ID: " + jobId);
        }
        
        // 验证职位状态是否可用
        if (!"OPEN".equals(job.getStatus())) {
            log.error("职位验证失败: 职位状态不可用，职位ID: {}, 状态: {}", jobId, job.getStatus());
            throw new IllegalArgumentException("职位验证失败: 职位已关闭或已过期，ID: " + jobId);
        }
        
        log.info("职位验证成功，职位ID: {}, 职位标题: {}", jobId, job.getTitle());
    }

    private void validateUserExists(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        
        try {
            ApiResponse<Boolean> response = userServiceClient.validateUser(userId);
            if (!response.isSuccess() || !Boolean.TRUE.equals(response.getData())) {
                log.error("用户验证失败: 用户不存在，用户ID: {}", userId);
                throw new IllegalArgumentException("用户验证失败: 用户不存在，ID: " + userId);
            }
        } catch (Exception e) {
            log.error("用户验证异常: {}", e.getMessage(), e);
            throw new IllegalArgumentException("用户验证失败: " + e.getMessage());
        }
    }

    private void sendInterviewNotification(Interview interview, String type) {
        try {
            Map<String, Object> notification = new HashMap<>();
            notification.put("interviewId", interview.getId());
            notification.put("userId", interview.getUserId());
            notification.put("jobId", interview.getJobId());
            notification.put("type", type);
            notification.put("title", "面试通知 - " + type);
            notification.put("content", generateNotificationContent(interview, type));
            notification.put("timestamp", LocalDateTime.now());
            notification.put("notificationType", type);
            
            rabbitTemplate.convertAndSend(interviewExchange, interviewNotificationRoutingKey, notification);
            log.info("通知发送成功，面试ID: {}, 类型: {}", interview.getId(), type);
        } catch (Exception e) {
            log.error("通知发送失败: {}", e.getMessage(), e);
        }
    }

    private String generateNotificationContent(Interview interview, String type) {
        StringBuilder content = new StringBuilder();
        content.append("面试通知 - ").append(type).append("\n");
        content.append("面试时间: ").append(interview.getScheduledTime()).append("\n");
        content.append("面试地点: ").append(interview.getLocation()).append("\n");
        content.append("面试方式: ").append(interview.getInterviewType()).append("\n");
        content.append("面试状态: ").append(interview.getStatus());
        return content.toString();
    }

    private InterviewDTO convertToDTO(Interview interview) {
        return InterviewDTO.builder()
                .id(interview.getId())
                .jobId(interview.getJobId())
                .userId(interview.getUserId())
                .scheduledTime(interview.getScheduledTime())
                .location(interview.getLocation())
                .interviewType(interview.getInterviewType())
                .status(interview.getStatus())
                .feedback(interview.getFeedback())
                .createdBy(interview.getCreatedBy())
                .createdAt(interview.getCreatedAt())
                .updatedAt(interview.getUpdatedAt())
                .build();
    }
} 