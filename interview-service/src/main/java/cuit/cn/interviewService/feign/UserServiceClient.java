package cuit.cn.interviewService.feign;

import cuit.cn.interviewService.dto.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", fallback = UserServiceClientFallback.class)
public interface UserServiceClient {
    
    @GetMapping("/user/exists/{id}")
    ApiResponse<Boolean> validateUser(@PathVariable("id") Long id);
} 