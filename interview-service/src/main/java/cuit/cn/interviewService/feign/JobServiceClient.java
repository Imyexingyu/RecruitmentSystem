package cuit.cn.interviewService.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;
import java.util.Map;

@FeignClient(name = "job-service", fallback = JobServiceClientFallback.class)
public interface JobServiceClient {
    
    @GetMapping("/api/jobs/{id}")
    ResponseEntity<Map<String, Object>> getJobById(@PathVariable("id") Long id);
} 