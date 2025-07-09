package cuit.cn.interviewService.feign;

import cuit.cn.interviewService.dto.ApiResponse;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UserServiceClientFallback implements UserServiceClient {
    
    @Override
    public ApiResponse<Boolean> validateUser(Long id) {
        log.error("用户服务不可用，用户ID: {}", id);
        return ApiResponse.<Boolean>builder()
                .success(false)
                .message("用户服务不可用")
                .data(false)
                .build();
    }
} 