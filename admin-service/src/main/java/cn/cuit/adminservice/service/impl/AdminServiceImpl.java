package cn.cuit.adminservice.service.impl;

import cn.cuit.adminservice.entity.JobStatistics;
import cn.cuit.adminservice.entity.UserStatistics;
import cn.cuit.adminservice.feign.JobServiceClient;
import cn.cuit.adminservice.feign.UserServiceClient;
import cn.cuit.adminservice.repository.JobStatisticsRepository;
import cn.cuit.adminservice.repository.UserStatisticsRepository;
import cn.cuit.adminservice.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserStatisticsRepository userStatisticsRepository;

    @Autowired
    private JobStatisticsRepository jobStatisticsRepository;

    @Autowired
    private UserServiceClient userServiceClient;

    @Autowired
    private JobServiceClient jobServiceClient;

    @Override
    public UserStatistics getUserStatistics() {
        UserStatistics statistics = userStatisticsRepository.findTopByOrderByLastUpdatedDesc();
        return statistics != null ? statistics : updateUserStatistics();
    }

    @Override
    public UserStatistics updateUserStatistics() {
        Map<String, Object> userCounts = userServiceClient.getUserCounts();
        
        UserStatistics statistics = new UserStatistics();
        statistics.setTotalUsers((Integer) userCounts.getOrDefault("totalUsers", 0));
        statistics.setCandidateUsers((Integer) userCounts.getOrDefault("candidateUsers", 0));
        statistics.setEmployerUsers((Integer) userCounts.getOrDefault("employerUsers", 0));
        statistics.setBlockedUsers((Integer) userCounts.getOrDefault("blockedUsers", 0));
        statistics.setLastUpdated(LocalDateTime.now());
        
        return userStatisticsRepository.save(statistics);
    }

    @Override
    public JobStatistics getJobStatistics() {
        JobStatistics statistics = jobStatisticsRepository.findTopByOrderByLastUpdatedDesc();
        return statistics != null ? statistics : updateJobStatistics();
    }

    @Override
    public JobStatistics updateJobStatistics() {
        Map<String, Object> jobCounts = jobServiceClient.getJobCounts();
        
        JobStatistics statistics = new JobStatistics();
        statistics.setTotalJobs((Integer) jobCounts.getOrDefault("totalJobs", 0));
        statistics.setActiveJobs((Integer) jobCounts.getOrDefault("activeJobs", 0));
        statistics.setClosedJobs((Integer) jobCounts.getOrDefault("closedJobs", 0));
        statistics.setTotalApplications((Integer) jobCounts.getOrDefault("totalApplications", 0));
        statistics.setPendingApplications((Integer) jobCounts.getOrDefault("pendingApplications", 0));
        statistics.setAcceptedApplications((Integer) jobCounts.getOrDefault("acceptedApplications", 0));
        statistics.setRejectedApplications((Integer) jobCounts.getOrDefault("rejectedApplications", 0));
        statistics.setLastUpdated(LocalDateTime.now());
        
        return jobStatisticsRepository.save(statistics);
    }

    @Override
    public Map<String, Object> blockUser(Long userId) {
        return userServiceClient.blockUser(userId);
    }

    @Override
    public Map<String, Object> unblockUser(Long userId) {
        return userServiceClient.unblockUser(userId);
    }

    @Override
    public Map<String, Object> deleteUser(Long userId) {
        return userServiceClient.deleteUser(userId);
    }

    @Override
    public Map<String, Object> deleteJob(Long jobId) {
        return jobServiceClient.deleteJob(jobId);
    }
} 