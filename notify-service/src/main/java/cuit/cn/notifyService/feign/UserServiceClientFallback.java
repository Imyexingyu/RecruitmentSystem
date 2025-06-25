package cuit.cn.notifyService.feign;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
public class UserServiceClientFallback implements UserServiceClient {
    
    @Override
    public ResponseEntity<Map<String, Object>> getUserById(Long id) {
        Map<String, Object> fallbackResponse = new HashMap<>();
        fallbackResponse.put("error", "用户服务不可用，请稍后再试");
        fallbackResponse.put("userId", id);
        return ResponseEntity.ok(fallbackResponse);
    }
} 