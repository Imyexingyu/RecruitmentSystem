package cuit.cn.interviewService.feign;

import cuit.cn.interviewService.dto.JobDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "job-service", fallback = JobServiceClientFallback.class)
public interface JobServiceClient {
    
    /**
     * 根据ID获取职位信息
     * @param id 职位ID
     * @return 职位信息，如果不存在则返回null
     */
    @GetMapping("/jobs/{id}")
    JobDTO getJobById(@PathVariable("id") Long id);
} 