package cuit.cn.interviewService.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.ResponseEntity;
import java.util.Map;

@FeignClient(name = "application-service", fallback = ApplicationServiceClientFallback.class)
public interface ApplicationServiceClient {
    
    @GetMapping("/api/applications/{id}")
    ResponseEntity<Map<String, Object>> getApplicationById(@PathVariable("id") Long id);
    
    @PutMapping("/api/applications/{id}/status")
    ResponseEntity<Map<String, Object>> updateApplicationStatus(
            @PathVariable("id") Long id, 
            @RequestParam("status") String status);
} 