package cn.cuit.adminservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "job-service")
public interface JobServiceClient {

    @GetMapping("/jobs/statistics")
    Map<String, Object> getJobCounts();

    @DeleteMapping("/jobs/{jobId}")
    Map<String, Object> deleteJob(@PathVariable("jobId") Long jobId);
}