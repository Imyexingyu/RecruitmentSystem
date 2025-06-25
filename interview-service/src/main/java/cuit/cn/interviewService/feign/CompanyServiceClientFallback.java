package cuit.cn.interviewService.feign;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
public class CompanyServiceClientFallback implements CompanyServiceClient {
    
    @Override
    public ResponseEntity<Map<String, Object>> getCompanyById(Long id) {
        Map<String, Object> fallbackResponse = new HashMap<>();
        fallbackResponse.put("error", "公司服务不可用，请稍后再试");
        fallbackResponse.put("companyId", id);
        return ResponseEntity.ok(fallbackResponse);
    }
} 