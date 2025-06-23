package cuit.cn.interviewService.feign;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
public class JobServiceClientFallback implements JobServiceClient {
    
    @Override
    public ResponseEntity<Map<String, Object>> getJobById(Long id) {
        Map<String, Object> fallbackResponse = new HashMap<>();
        fallbackResponse.put("error", "职位服务不可用，请稍后再试");
        fallbackResponse.put("jobId", id);
        return ResponseEntity.ok(fallbackResponse);
    }
} 