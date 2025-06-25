package cuit.cn.interviewService.feign;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
public class ApplicationServiceClientFallback implements ApplicationServiceClient {
    
    @Override
    public ResponseEntity<Map<String, Object>> getApplicationById(Long id) {
        Map<String, Object> fallbackResponse = new HashMap<>();
        fallbackResponse.put("error", "申请服务不可用，请稍后再试");
        fallbackResponse.put("applicationId", id);
        return ResponseEntity.ok(fallbackResponse);
    }
    
    @Override
    public ResponseEntity<Map<String, Object>> updateApplicationStatus(Long id, String status) {
        Map<String, Object> fallbackResponse = new HashMap<>();
        fallbackResponse.put("error", "申请服务不可用，无法更新申请状态，请稍后再试");
        fallbackResponse.put("applicationId", id);
        fallbackResponse.put("status", status);
        return ResponseEntity.ok(fallbackResponse);
    }
} 