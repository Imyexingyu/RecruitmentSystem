package cn.cuit.jobservice.Service;

import cn.cuit.jobservice.Entity.JobPost;

import java.util.List;
import java.util.Map;

public interface JobService {
    int publish(JobPost post);
    List<JobPost> listByEmployer(Long employerId);
    List<JobPost> searchJobs(String keyword);
    int deleteById(Long id);
    JobPost getById(Long id);
    int update(JobPost post);
    List<JobPost> getAllOpenJobs();
    JobPost getJobDetail(Long jobId);
    Map<String, Object> getJobStatistics();
    Map<String, Object> deleteJob(Long jobId);
}

