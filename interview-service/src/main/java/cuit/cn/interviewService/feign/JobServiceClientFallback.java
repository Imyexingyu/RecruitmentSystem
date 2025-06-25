package cuit.cn.interviewService.feign;

import cuit.cn.interviewService.dto.JobDTO;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JobServiceClientFallback implements JobServiceClient {
    
    @Override
    public JobDTO getJobById(Long id) {
        log.error("职位服务不可用，职位ID: {}", id);
        
        // 创建一个带有错误状态的Job对象
        JobDTO jobDTO = new JobDTO();
        jobDTO.setId(id);
        jobDTO.setTitle("职位服务不可用");
        jobDTO.setStatus("ERROR");
        jobDTO.setDescription("职位服务当前不可用，请稍后再试");
        
        return jobDTO;
    }
} 