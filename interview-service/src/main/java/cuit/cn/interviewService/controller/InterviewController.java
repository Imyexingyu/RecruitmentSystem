package cuit.cn.interviewService.controller;

import cuit.cn.interviewService.dto.InterviewDTO;
import cuit.cn.interviewService.dto.InterviewRequest;
import cuit.cn.interviewService.dto.InterviewResponse;
import cuit.cn.interviewService.dto.InterviewUpdateRequest;
import cuit.cn.interviewService.service.InterviewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/interviews")
@Api(tags = "面试管理接口")
public class InterviewController {
    
    private final InterviewService interviewService;
    
    @Autowired
    public InterviewController(InterviewService interviewService) {
        this.interviewService = interviewService;
    }
    
    @PostMapping
    @ApiOperation("安排面试")
    public ResponseEntity<InterviewResponse<InterviewDTO>> scheduleInterview(
            @Valid @RequestBody InterviewRequest request,
            BindingResult bindingResult) {
        try {
            // 处理参数验证错误
            if (bindingResult.hasErrors()) {
                String errorMessage = bindingResult.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .collect(Collectors.joining(", "));
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(InterviewResponse.fail(400, "参数验证失败: " + errorMessage));
            }

            // 验证面试方式
            if (request.getInterviewMethod() != null && 
                !request.getInterviewMethod().equalsIgnoreCase("ONLINE") && 
                !request.getInterviewMethod().equalsIgnoreCase("OFFLINE")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(InterviewResponse.fail(400, "面试方式必须是 ONLINE 或 OFFLINE"));
            }

            // 验证面试时间
            if (request.getScheduleTime() != null && 
                request.getScheduleTime().isBefore(java.time.LocalDateTime.now())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(InterviewResponse.fail(400, "面试时间不能早于当前时间"));
            }

            InterviewDTO interview = interviewService.scheduleInterview(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(InterviewResponse.success("面试安排成功", interview));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(InterviewResponse.fail(400, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(InterviewResponse.fail(e.getMessage()));
        }
    }
    
    @GetMapping("/{id}")
    @ApiOperation("获取面试详情")
    public ResponseEntity<InterviewResponse<InterviewDTO>> getInterview(@PathVariable Long id) {
        try {
            InterviewDTO interview = interviewService.getInterview(id);
            return ResponseEntity.ok(InterviewResponse.success(interview));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(InterviewResponse.fail(404, e.getMessage()));
        }
    }
    
    @GetMapping("/application/{applicationId}")
    @ApiOperation("根据应聘记录ID获取面试列表")
    public ResponseEntity<InterviewResponse<Page<InterviewDTO>>> getInterviewsByApplicationId(
            @PathVariable Long applicationId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("scheduleTime").descending());
            Page<InterviewDTO> interviews = interviewService.getInterviewsByApplicationId(applicationId, pageable);
            return ResponseEntity.ok(InterviewResponse.success(interviews));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(InterviewResponse.fail(e.getMessage()));
        }
    }
    
    @PutMapping("/{id}")
    @ApiOperation("更新面试信息")
    public ResponseEntity<InterviewResponse<InterviewDTO>> updateInterview(
            @PathVariable Long id,
            @Valid @RequestBody InterviewUpdateRequest request,
            BindingResult bindingResult) {
        try {
            // 处理参数验证错误
            if (bindingResult.hasErrors()) {
                String errorMessage = bindingResult.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .collect(Collectors.joining(", "));
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(InterviewResponse.fail(400, "参数验证失败: " + errorMessage));
            }

            // 验证面试方式
            if (request.getInterviewMethod() != null && 
                !request.getInterviewMethod().equalsIgnoreCase("ONLINE") && 
                !request.getInterviewMethod().equalsIgnoreCase("OFFLINE")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(InterviewResponse.fail(400, "面试方式必须是 ONLINE 或 OFFLINE"));
            }

            // 验证面试时间
            if (request.getScheduleTime() != null && 
                request.getScheduleTime().isBefore(java.time.LocalDateTime.now())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(InterviewResponse.fail(400, "面试时间不能早于当前时间"));
            }

            // 检查是否至少有一个字段需要更新
            if (request.getScheduleTime() == null && 
                request.getInterviewMethod() == null && 
                request.getLocation() == null && 
                request.getInterviewer() == null && 
                request.getFeedback() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(InterviewResponse.fail(400, "至少需要提供一个要更新的字段"));
            }

            InterviewDTO interview = interviewService.updateInterview(id, request);
            return ResponseEntity.ok(InterviewResponse.success("面试信息更新成功", interview));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(InterviewResponse.fail(400, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(InterviewResponse.fail(e.getMessage()));
        }
    }
    
    @PutMapping("/{id}/status")
    @ApiOperation("更新面试状态")
    public ResponseEntity<InterviewResponse<InterviewDTO>> updateInterviewStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        try {
            InterviewDTO interview = interviewService.updateInterviewStatus(id, status);
            return ResponseEntity.ok(InterviewResponse.success("面试状态更新成功", interview));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(InterviewResponse.fail(400, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(InterviewResponse.fail(e.getMessage()));
        }
    }
    
    @PutMapping("/{id}/feedback")
    @ApiOperation("添加面试反馈")
    public ResponseEntity<InterviewResponse<InterviewDTO>> addInterviewFeedback(
            @PathVariable Long id,
            @RequestParam String feedback) {
        try {
            if (feedback == null || feedback.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(InterviewResponse.fail(400, "面试反馈不能为空"));
            }
            InterviewDTO interview = interviewService.addInterviewFeedback(id, feedback);
            return ResponseEntity.ok(InterviewResponse.success("面试反馈添加成功", interview));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(InterviewResponse.fail(e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    @ApiOperation("取消面试")
    public ResponseEntity<InterviewResponse<Void>> cancelInterview(@PathVariable Long id) {
        try {
            interviewService.cancelInterview(id);
            return ResponseEntity.ok(InterviewResponse.success("面试已取消", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(InterviewResponse.fail(e.getMessage()));
        }
    }
} 