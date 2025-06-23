package cuit.cn.interviewService.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;
import java.util.Map;

@FeignClient(name = "company-service", fallback = CompanyServiceClientFallback.class)
public interface CompanyServiceClient {
    
    @GetMapping("/api/companies/{id}")
    ResponseEntity<Map<String, Object>> getCompanyById(@PathVariable("id") Long id);
} 