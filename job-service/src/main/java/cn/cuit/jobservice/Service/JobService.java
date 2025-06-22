package cn.cuit.jobservice.Service;

import cn.cuit.jobservice.Entity.JobPost;

import java.util.List;

public interface JobService {
    int publish(JobPost post);
    List<JobPost> listByEmployer(Long employerId);
    List<JobPost> searchJobs(String keyword);
    int deleteById(Long id);
    JobPost getById(Long id);
    int update(JobPost post);
}

