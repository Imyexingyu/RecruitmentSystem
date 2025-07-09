package cuit.cn.interviewService.controller;

import cuit.cn.interviewService.dto.ApiResponse;
import cuit.cn.interviewService.dto.InterviewDTO;
import cuit.cn.interviewService.dto.InterviewRequest;
import cuit.cn.interviewService.model.InterviewStatus;
import cuit.cn.interviewService.service.InterviewService;
import cuit.cn.interviewService.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/interviews")
@Api(tags = "面试管理接口")
public class InterviewController {
    
    private final InterviewService interviewService;
    private final JwtUtil jwtUtil;
    
    @Autowired
    public InterviewController(InterviewService interviewService, JwtUtil jwtUtil) {
        this.interviewService = interviewService;
        this.jwtUtil = jwtUtil;
    }
    
    /**
     * 从请求中获取JWT令牌
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        // 首先从请求头中获取
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        
        // 如果请求头中没有，则从Cookie中获取
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        
        return null;
    }
    
    @GetMapping("/all")
    @ApiOperation("获取所有面试")
    public ResponseEntity<ApiResponse<List<InterviewDTO>>> getAllInterviews(
            HttpServletRequest request) {
        
        // 获取并验证令牌
        String token = getTokenFromRequest(request);
        if (token == null || !jwtUtil.validateToken(token)) {
            log.error("未提供有效的JWT令牌");
            return ResponseEntity.status(401).body(ApiResponse.error("未提供有效的JWT令牌，请重新登录"));
        }
        
        // 从令牌中获取用户信息
        String username = jwtUtil.getUsernameFromToken(token);
        String role = jwtUtil.getRoleFromToken(token);
        
        log.info("获取所有面试列表，用户: {}, 角色: {}", username, role);
        
        // 验证用户是否有权限查看所有面试
        if (!isAuthorizedToViewJobInterviews(role)) {
            return ResponseEntity.status(403).body(ApiResponse.error("没有权限查看所有面试"));
        }
        
        try {
            List<InterviewDTO> interviews = interviewService.getAllInterviews();
            return ResponseEntity.ok(ApiResponse.success(interviews));
        } catch (Exception e) {
            log.error("获取所有面试时发生错误", e);
            return ResponseEntity.status(500).body(ApiResponse.error("获取面试列表失败: " + e.getMessage()));
        }
    }
    
    @PostMapping
    @ApiOperation("创建面试")
    public ResponseEntity<ApiResponse<InterviewDTO>> createInterview(
            @Valid @RequestBody InterviewRequest request,
            HttpServletRequest httpRequest) {

        // 获取并验证令牌
        String token = getTokenFromRequest(httpRequest);
        if (token == null || !jwtUtil.validateToken(token)) {
            log.error("未提供有效的JWT令牌");
            return ResponseEntity.status(401).body(ApiResponse.error("未提供有效的JWT令牌，请重新登录"));
        }

        // 从令牌中获取用户信息
        String username = jwtUtil.getUsernameFromToken(token);
        String role = jwtUtil.getRoleFromToken(token);
        Long currentUserId = jwtUtil.getUserIdFromToken(token);

        log.info("收到创建面试请求，用户: {}, 角色: {}, 用户ID: {}", username, role, currentUserId);
        System.out.println("111"+currentUserId+" "+ role + " " + username);
//        try {
//            // 验证用户是否有权限创建面试
//            if (!isAuthorizedToScheduleInterview(role)) {
//                return ResponseEntity.status(403).body(ApiResponse.error("没有权限创建面试"));
//            }
//
//            InterviewDTO interview = interviewService.createInterview(request, currentUserId);
//            return ResponseEntity.ok(ApiResponse.success(interview));
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
//        }
        InterviewDTO interview = interviewService.createInterview(request, currentUserId);
        return ResponseEntity.ok(ApiResponse.success(interview));
    }
    
    /**
     * 验证用户是否有权限安排面试
     */
    private boolean isAuthorizedToScheduleInterview(String role) {
        return role != null && (role.equals("admin") || role.equals("hr") || role.equals("recruiter") || role.equals("employer") || role.equals("SERVICE"));
    }
    
    @GetMapping("/{id}")
    @ApiOperation("获取面试详情")
    public ResponseEntity<ApiResponse<InterviewDTO>> getInterview(
            @PathVariable Long id,
            @RequestHeader(value = "username", required = false) String username,
            @RequestHeader(value = "role", required = false) String role,
            HttpServletRequest request) {
        
        // 获取并验证令牌
        String token = getTokenFromRequest(request);
        if (token == null || !jwtUtil.validateToken(token)) {
            log.error("未提供有效的JWT令牌");
            return ResponseEntity.status(401).body(ApiResponse.error("未提供有效的JWT令牌，请重新登录"));
        }
        
        // 从令牌中获取用户信息
        username = jwtUtil.getUsernameFromToken(token);
        role = jwtUtil.getRoleFromToken(token);
        Long userId = jwtUtil.getUserIdFromToken(token);
        
        log.info("获取面试详情，ID: {}, 用户: {}, 角色: {}, 用户ID: {}", id, username, role, userId);
        
        try {
            InterviewDTO interview = interviewService.getInterview(id);
            
            // 验证用户是否有权限访问该面试信息
            if (!isAuthorizedToViewInterview(role, userId, interview)) {
                return ResponseEntity.status(403).body(ApiResponse.error("没有权限访问该面试信息"));
            }
            
            return ResponseEntity.ok(ApiResponse.success(interview));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * 验证用户是否有权限查看面试详情
     */
    private boolean isAuthorizedToViewInterview(String role, Long userId, InterviewDTO interview) {
        // 管理员、HR、招聘人员和雇主可以查看所有面试
        if (role != null && (role.equals("admin") || role.equals("hr") || role.equals("recruiter") || role.equals("employer") || role.equals("SERVICE"))) {
            return true;
        }
        
        // 求职者只能查看自己的面试
        return userId != null && interview.getUserId() != null && userId.equals(interview.getUserId());
    }
    
    @GetMapping("/job/{jobId}")
    @ApiOperation("根据职位ID获取面试列表")
    public ResponseEntity<ApiResponse<Page<InterviewDTO>>> getInterviewsByJobId(
            @PathVariable Long jobId,
            Pageable pageable,
            @RequestHeader(value = "username", required = false) String username,
            @RequestHeader(value = "role", required = false) String role,
            HttpServletRequest request) {
        
        // 获取并验证令牌
        String token = getTokenFromRequest(request);
        if (token == null || !jwtUtil.validateToken(token)) {
            log.error("未提供有效的JWT令牌");
            return ResponseEntity.status(401).body(ApiResponse.error("未提供有效的JWT令牌，请重新登录"));
        }
        
        // 从令牌中获取用户信息
        username = jwtUtil.getUsernameFromToken(token);
        role = jwtUtil.getRoleFromToken(token);
        
        log.info("获取职位面试列表，职位ID: {}, 用户: {}, 角色: {}", jobId, username, role);
        
        // 验证用户是否有权限查看该职位的面试列表
        if (!isAuthorizedToViewJobInterviews(role)) {
            return ResponseEntity.status(403).body(ApiResponse.error("没有权限查看该职位的面试列表"));
        }
        
        try {
            Page<InterviewDTO> interviews = interviewService.getInterviewsByJobId(jobId, pageable);
            return ResponseEntity.ok(ApiResponse.success(interviews));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * 验证用户是否有权限查看职位的面试列表
     */
    private boolean isAuthorizedToViewJobInterviews(String role) {
        // 只有管理员、HR、招聘人员和雇主可以查看职位的面试列表
        return role != null && (role.equals("admin") || role.equals("hr") || role.equals("recruiter") || role.equals("employer") || role.equals("SERVICE"));
    }
    
    @GetMapping("/user/{userId}")
    @ApiOperation("根据用户ID获取面试列表")
    public ResponseEntity<ApiResponse<List<InterviewDTO>>> getInterviewsByUser(
            @PathVariable Long userId,
            HttpServletRequest request) {
        
        // 获取并验证令牌
        String token = getTokenFromRequest(request);
        if (token == null || !jwtUtil.validateToken(token)) {
            log.error("未提供有效的JWT令牌");
            return ResponseEntity.status(401).body(ApiResponse.error("未提供有效的JWT令牌，请重新登录"));
        }
        
        // 从令牌中获取用户信息
        String username = jwtUtil.getUsernameFromToken(token);
        String role = jwtUtil.getRoleFromToken(token);
        Long currentUserId = jwtUtil.getUserIdFromToken(token);
        
        log.info("获取用户面试列表，用户ID: {}, 当前用户: {}, 角色: {}", userId, username, role);
        
        // 验证用户是否有权限查看该用户的面试列表
        // 管理员可以查看所有人的面试，普通用户只能查看自己的面试
        if (!role.equals("admin") && !currentUserId.equals(userId)) {
            return ResponseEntity.status(403).body(ApiResponse.error("没有权限查看该用户的面试列表"));
        }
        
        try {
            List<InterviewDTO> interviews = interviewService.getInterviewsByCandidate(userId);
            return ResponseEntity.ok(ApiResponse.success(interviews));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PutMapping("/{id}/status")
    @ApiOperation("更新面试状态")
    public ResponseEntity<ApiResponse<InterviewDTO>> updateInterviewStatus(
            @PathVariable Long id,
            @RequestParam String status,
            HttpServletRequest request) {
        
        // 获取并验证令牌
        String token = getTokenFromRequest(request);
        if (token == null || !jwtUtil.validateToken(token)) {
            log.error("未提供有效的JWT令牌");
            return ResponseEntity.status(401).body(ApiResponse.error("未提供有效的JWT令牌，请重新登录"));
        }
        
        // 从令牌中获取用户信息
        String username = jwtUtil.getUsernameFromToken(token);
        String role = jwtUtil.getRoleFromToken(token);
        
        log.info("更新面试状态，ID: {}, 状态: {}, 用户: {}, 角色: {}", id, status, username, role);
        
        // 验证用户是否有权限更新面试状态
        if (!isAuthorizedToUpdateInterviewStatus(role)) {
            return ResponseEntity.status(403).body(ApiResponse.error("没有权限更新面试状态"));
        }
        
        try {
            InterviewDTO interview = interviewService.updateInterviewStatus(id, status);
            return ResponseEntity.ok(ApiResponse.success(interview));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * 验证用户是否有权限更新面试状态
     */
    private boolean isAuthorizedToUpdateInterviewStatus(String role) {
        // 只有管理员、HR、招聘人员和雇主可以更新面试状态
        return role != null && (role.equals("admin") || role.equals("hr") || role.equals("recruiter") || role.equals("employer") || role.equals("SERVICE"));
    }
    
    @PutMapping("/{id}/feedback")
    @ApiOperation("添加面试反馈")
    public ResponseEntity<ApiResponse<InterviewDTO>> addFeedback(
            @PathVariable Long id,
            @RequestParam String feedback,
            @RequestHeader(value = "username", required = false) String username,
            @RequestHeader(value = "role", required = false) String role,
            HttpServletRequest request) {
        
        // 获取并验证令牌
        String token = getTokenFromRequest(request);
        if (token == null || !jwtUtil.validateToken(token)) {
            log.error("未提供有效的JWT令牌");
            return ResponseEntity.status(401).body(ApiResponse.error("未提供有效的JWT令牌，请重新登录"));
        }
        
        // 从令牌中获取用户信息
        username = jwtUtil.getUsernameFromToken(token);
        role = jwtUtil.getRoleFromToken(token);
        
        log.info("添加面试反馈，ID: {}, 用户: {}, 角色: {}", id, username, role);
        
        // 验证用户是否有权限添加面试反馈
        if (!isAuthorizedToAddFeedback(role)) {
            return ResponseEntity.status(403).body(ApiResponse.error("没有权限添加面试反馈"));
        }
        
        try {
            InterviewDTO interview = interviewService.addInterviewFeedback(id, feedback);
            return ResponseEntity.ok(ApiResponse.success(interview));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * 验证用户是否有权限添加面试反馈
     */
    private boolean isAuthorizedToAddFeedback(String role) {
        // 只有管理员、HR、招聘人员和雇主可以添加面试反馈
        return role != null && (role.equals("admin") || role.equals("hr") || role.equals("recruiter") || role.equals("employer") || role.equals("SERVICE"));
    }
} 