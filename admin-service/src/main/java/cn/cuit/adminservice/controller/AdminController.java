package cn.cuit.adminservice.controller;

import cn.cuit.adminservice.entity.JobStatistics;
import cn.cuit.adminservice.entity.UserStatistics;
import cn.cuit.adminservice.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    // 统计数据相关接口
    @GetMapping("/statistics/users")
    public ResponseEntity<UserStatistics> getUserStatistics() {
        return ResponseEntity.ok(adminService.getUserStatistics());
    }

    @PostMapping("/statistics/users/refresh")
    public ResponseEntity<UserStatistics> refreshUserStatistics() {
        return ResponseEntity.ok(adminService.updateUserStatistics());
    }

    @GetMapping("/statistics/jobs")
    public ResponseEntity<JobStatistics> getJobStatistics() {
        return ResponseEntity.ok(adminService.getJobStatistics());
    }

    @PostMapping("/statistics/jobs/refresh")
    public ResponseEntity<JobStatistics> refreshJobStatistics() {
        return ResponseEntity.ok(adminService.updateJobStatistics());
    }

    // 用户管理相关接口
    @PostMapping("/users/{userId}/block")
    public ResponseEntity<Map<String, Object>> blockUser(@PathVariable Long userId) {
        return ResponseEntity.ok(adminService.blockUser(userId));
    }

    @PostMapping("/users/{userId}/unblock")
    public ResponseEntity<Map<String, Object>> unblockUser(@PathVariable Long userId) {
        return ResponseEntity.ok(adminService.unblockUser(userId));
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable Long userId) {
        return ResponseEntity.ok(adminService.deleteUser(userId));
    }

    // 职位管理相关接口
    @DeleteMapping("/jobs/{jobId}")
    public ResponseEntity<Map<String, Object>> deleteJob(@PathVariable Long jobId) {
        return ResponseEntity.ok(adminService.deleteJob(jobId));
    }

    // 系统状态
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getSystemStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "running");
        status.put("service", "admin-service");
        status.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(status);
    }
} 