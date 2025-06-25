package cn.cuit.adminservice.service;

import cn.cuit.adminservice.entity.JobStatistics;
import cn.cuit.adminservice.entity.UserStatistics;

import java.util.Map;

public interface AdminService {
    
    // 用户统计相关
    UserStatistics getUserStatistics();
    
    UserStatistics updateUserStatistics();
    
    // 职位统计相关
    JobStatistics getJobStatistics();
    
    JobStatistics updateJobStatistics();
    
    // 用户管理相关
    Map<String, Object> blockUser(Long userId);
    
    Map<String, Object> unblockUser(Long userId);
    
    Map<String, Object> deleteUser(Long userId);
    
    // 职位管理相关
    Map<String, Object> deleteJob(Long jobId);
} 