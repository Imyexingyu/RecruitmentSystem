package cuit.cn.notifyService.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;
import java.util.Map;

@FeignClient(name = "user-service", fallback = UserServiceClientFallback.class)
public interface UserServiceClient {
    
    @GetMapping("/user/exists/{id}")
    ResponseEntity<Map<String, Object>> getUserById(@PathVariable("id") Long id);
} 