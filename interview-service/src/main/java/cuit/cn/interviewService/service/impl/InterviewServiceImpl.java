package cuit.cn.interviewService.service.impl;

import cuit.cn.interviewService.dto.InterviewDTO;
import cuit.cn.interviewService.dto.InterviewRequest;
import cuit.cn.interviewService.dto.InterviewUpdateRequest;
import cuit.cn.interviewService.feign.ApplicationServiceClient;
import cuit.cn.interviewService.feign.CompanyServiceClient;
import cuit.cn.interviewService.feign.JobServiceClient;
import cuit.cn.interviewService.feign.UserServiceClient;
import cuit.cn.interviewService.model.Interview;
import cuit.cn.interviewService.model.InterviewStatus;
import cuit.cn.interviewService.repository.InterviewRepository;
import cuit.cn.interviewService.service.InterviewService;
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
import java.util.logging.Logger;

@Service
public class InterviewServiceImpl implements InterviewService {
    
    private static final Logger logger = Logger.getLogger(InterviewServiceImpl.class.getName());

    private final InterviewRepository interviewRepository;
    private final RabbitTemplate rabbitTemplate;
    private final UserServiceClient userServiceClient;
    private final JobServiceClient jobServiceClient;
    private final CompanyServiceClient companyServiceClient;
    private final ApplicationServiceClient applicationServiceClient;

    @Value("${rabbitmq.exchange.interview}")
    private String interviewExchange;

    @Value("${rabbitmq.routing-key.interview-notification}")
    private String interviewNotificationRoutingKey;

    @Autowired
    public InterviewServiceImpl(
            InterviewRepository interviewRepository, 
            RabbitTemplate rabbitTemplate,
            UserServiceClient userServiceClient,
            JobServiceClient jobServiceClient,
            CompanyServiceClient companyServiceClient,
            ApplicationServiceClient applicationServiceClient) {
        this.interviewRepository = interviewRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.userServiceClient = userServiceClient;
        this.jobServiceClient = jobServiceClient;
        this.companyServiceClient = companyServiceClient;
        this.applicationServiceClient = applicationServiceClient;
    }

    @Override
    @Transactional
    public InterviewDTO scheduleInterview(InterviewRequest request) {
        // 数据校验
        if (request.getJobId() == null || request.getUserId() == null || request.getCompanyId() == null) {
            throw new IllegalArgumentException("职位ID、用户ID和公司ID不能为空");
        }
        
        // 与职位服务集成，验证职位是否存在
        jobServiceClient.getJobById(request.getJobId());
        
        // 与用户服务集成，验证用户是否存在
        userServiceClient.getUserById(request.getUserId());
        
        // 与公司服务集成，验证公司是否存在
        companyServiceClient.getCompanyById(request.getCompanyId());
        
        Interview interview = new Interview();
        interview.setJobId(request.getJobId());
        interview.setUserId(request.getUserId());
        interview.setCompanyId(request.getCompanyId());
        interview.setApplicationId(request.getApplicationId());
        interview.setScheduleTime(request.getScheduleTime());
        interview.setInterviewMethod(request.getInterviewMethod());
        interview.setLocation(request.getLocation());
        interview.setInterviewer(request.getInterviewer());
        interview.setStatus(InterviewStatus.SCHEDULED);
        interview.setCreatedAt(LocalDateTime.now());
        interview.setUpdatedAt(LocalDateTime.now());
        
        Interview savedInterview = interviewRepository.save(interview);
        
        // 发送面试通知
        sendInterviewNotification(savedInterview, "新面试安排");
        
        return convertToDTO(savedInterview);
    }

    @Override
    public InterviewDTO getInterview(Long id) {
        Interview interview = getInterviewById(id);
        return convertToDTO(interview);
    }

    @Override
    public Page<InterviewDTO> getInterviewsByApplicationId(Long applicationId, Pageable pageable) {
        // TODO: 与申请服务集成，验证申请是否存在
        Page<Interview> interviews = interviewRepository.findByApplicationId(applicationId, pageable);
        return interviews.map(this::convertToDTO);
    }

    @Override
    @Transactional
    public InterviewDTO updateInterview(Long id, InterviewUpdateRequest request) {
        Interview interview = getInterviewById(id);
        
        // 保存原始面试时间，用于判断是否变更了面试时间
        LocalDateTime originalScheduleTime = interview.getScheduleTime();
        
        // 更新字段，只更新非空字段
        if (request.getScheduleTime() != null) {
            interview.setScheduleTime(request.getScheduleTime());
        }
        if (request.getInterviewMethod() != null) {
            interview.setInterviewMethod(request.getInterviewMethod().toUpperCase());
        }
        if (request.getLocation() != null) {
            interview.setLocation(request.getLocation());
        }
        if (request.getInterviewer() != null) {
            interview.setInterviewer(request.getInterviewer());
        }
        if (request.getFeedback() != null) {
            interview.setFeedback(request.getFeedback());
        }
        
        interview.setUpdatedAt(LocalDateTime.now());
        Interview updatedInterview = interviewRepository.save(interview);
        
        // 如果面试时间变更，发送面试变更通知
        if (!originalScheduleTime.equals(updatedInterview.getScheduleTime())) {
            sendInterviewNotification(updatedInterview, "面试变更");
        } else {
            // 其他字段变更也发送通知
            sendInterviewNotification(updatedInterview, "面试信息更新");
        }
        
        return convertToDTO(updatedInterview);
    }

    @Override
    @Transactional
    public void cancelInterview(Long id) {
        Interview interview = getInterviewById(id);
        
        // 发送面试取消通知
        sendInterviewNotification(interview, "面试取消");
        
        // 与申请服务集成，更新申请状态
        if (interview.getApplicationId() != null) {
            applicationServiceClient.updateApplicationStatus(interview.getApplicationId(), "REJECTED");
        }
        
        interviewRepository.deleteById(id);
    }

    @Override
    @Transactional
    public InterviewDTO updateInterviewStatus(Long id, String status) {
        try {
        Interview interview = getInterviewById(id);
            InterviewStatus newStatus = InterviewStatus.valueOf(status);
            interview.setStatus(newStatus);
            interview.setUpdatedAt(LocalDateTime.now());
            
        Interview updatedInterview = interviewRepository.save(interview);
            
            // 发送面试状态变更通知
            sendInterviewNotification(updatedInterview, "面试状态更新");
            
            // 与申请服务集成，根据面试状态更新申请状态
            if (updatedInterview.getApplicationId() != null) {
                String applicationStatus = mapInterviewStatusToApplicationStatus(newStatus);
                applicationServiceClient.updateApplicationStatus(updatedInterview.getApplicationId(), applicationStatus);
            }
            
        return convertToDTO(updatedInterview);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("无效的面试状态: " + status);
        }
    }

    @Override
    @Transactional
    public InterviewDTO addInterviewFeedback(Long id, String feedback) {
        Interview interview = getInterviewById(id);
        interview.setFeedback(feedback);
        interview.setUpdatedAt(LocalDateTime.now());
        
        Interview updatedInterview = interviewRepository.save(interview);
        
        // TODO: 与评估服务集成，保存详细的面试评估信息
        
        return convertToDTO(updatedInterview);
    }

    @Override
    @Transactional
    public Interview createInterview(InterviewDTO interviewDTO) {
        // 数据校验
        if (interviewDTO.getJobId() == null || interviewDTO.getUserId() == null || interviewDTO.getCompanyId() == null) {
            throw new IllegalArgumentException("职位ID、用户ID和公司ID不能为空");
        }
        
        Interview interview = new Interview();
        BeanUtils.copyProperties(interviewDTO, interview);
        interview.setStatus(InterviewStatus.SCHEDULED);
        interview.setCreatedAt(LocalDateTime.now());
        interview.setUpdatedAt(LocalDateTime.now());
        
        Interview savedInterview = interviewRepository.save(interview);
        
        // 发送面试通知
        sendInterviewNotification(savedInterview, "新面试安排");
        
        return savedInterview;
    }

    @Override
    @Transactional
    public Interview updateInterview(Long id, InterviewDTO interviewDTO) {
        Interview interview = getInterviewById(id);
        
        // 保存原始面试时间，用于判断是否变更了面试时间
        LocalDateTime originalScheduleTime = interview.getScheduleTime();
        
        // 更新面试信息
        BeanUtils.copyProperties(interviewDTO, interview);
        interview.setId(id); // 确保ID不变
        interview.setUpdatedAt(LocalDateTime.now());
        
        Interview updatedInterview = interviewRepository.save(interview);
        
        // 如果面试时间变更，发送面试变更通知
        if (!originalScheduleTime.equals(updatedInterview.getScheduleTime())) {
            sendInterviewNotification(updatedInterview, "面试变更");
        }
        
        return updatedInterview;
    }

    @Override
    @Transactional
    public void deleteInterview(Long id) {
        Interview interview = getInterviewById(id);
        
        // 发送面试取消通知
        sendInterviewNotification(interview, "面试取消");
        
        // 与申请服务集成，更新申请状态
        if (interview.getApplicationId() != null) {
            applicationServiceClient.updateApplicationStatus(interview.getApplicationId(), "REJECTED");
        }
        
        interviewRepository.deleteById(id);
    }

    @Override
    public Interview getInterviewById(Long id) {
        return interviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("面试不存在，ID: " + id));
    }

    @Override
    public List<Interview> getInterviewsByJobId(Long jobId) {
        // 与职位服务集成，验证职位是否存在
        jobServiceClient.getJobById(jobId);
        return interviewRepository.findByJobId(jobId);
    }

    @Override
    public List<Interview> getInterviewsByUserId(Long userId) {
        // 与用户服务集成，验证用户是否存在
        userServiceClient.getUserById(userId);
        return interviewRepository.findByUserId(userId);
    }

    @Override
    public void sendInterviewNotification(Interview interview, String notificationType) {
        try {
            logger.info("准备发送面试通知: " + interview.getId() + ", 类型: " + notificationType);
            
        Map<String, Object> notification = new HashMap<>();
        notification.put("interviewId", interview.getId());
        notification.put("userId", interview.getUserId());
        notification.put("companyId", interview.getCompanyId());
        notification.put("scheduleTime", interview.getScheduleTime());
        notification.put("interviewMethod", interview.getInterviewMethod());
        notification.put("location", interview.getLocation());
        notification.put("interviewer", interview.getInterviewer());
        notification.put("notificationType", notificationType);
        notification.put("title", "面试通知");
        notification.put("content", generateNotificationContent(interview, notificationType));
        notification.put("type", "INTERVIEW");
        notification.put("createdAt", LocalDateTime.now());
            
            logger.info("发送消息到交换机: " + interviewExchange + ", 路由键: " + interviewNotificationRoutingKey);
        
        rabbitTemplate.convertAndSend(interviewExchange, interviewNotificationRoutingKey, notification);
            
            logger.info("面试通知已发送");
        } catch (Exception e) {
            // 记录日志，但不要让消息队列的问题影响主要业务流程
            logger.severe("发送通知失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private String generateNotificationContent(Interview interview, String notificationType) {
        StringBuilder content = new StringBuilder();
        
        switch (notificationType) {
            case "新面试安排":
                content.append("您有一个新的面试安排，详情如下：\n");
                break;
            case "面试变更":
                content.append("您的面试安排已变更，最新详情如下：\n");
                break;
            case "面试取消":
                content.append("您的面试已被取消，原面试详情如下：\n");
                break;
            case "面试状态更新":
                content.append("您的面试状态已更新为: ").append(interview.getStatus()).append("\n详情如下：\n");
                break;
            default:
                content.append("面试通知：\n");
                break;
        }
        
        content.append("面试时间：").append(interview.getScheduleTime()).append("\n");
        content.append("面试方式：").append(interview.getInterviewMethod()).append("\n");
        
        if (interview.getLocation() != null && !interview.getLocation().isEmpty()) {
            content.append("面试地点：").append(interview.getLocation()).append("\n");
        }
        
        if (interview.getInterviewer() != null && !interview.getInterviewer().isEmpty()) {
            content.append("面试官：").append(interview.getInterviewer()).append("\n");
        }
        
        return content.toString();
    }

    private InterviewDTO convertToDTO(Interview interview) {
        InterviewDTO dto = new InterviewDTO();
        BeanUtils.copyProperties(interview, dto);
        return dto;
    }
    
    /**
     * 将面试状态映射为申请状态
     */
    private String mapInterviewStatusToApplicationStatus(InterviewStatus interviewStatus) {
        switch (interviewStatus) {
            case SCHEDULED:
                return "INTERVIEWING";
            case COMPLETED:
                return "INTERVIEWED";
            case CANCELLED:
                return "REJECTED";
            case NO_SHOW:
                return "REJECTED";
            case IN_PROGRESS:
                return "INTERVIEWING";
            default:
                return "PROCESSING";
        }
    }
} 