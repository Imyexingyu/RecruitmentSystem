package cn.cuit.jobservice.Service;

import cn.cuit.jobservice.Entity.JobPost;
import cn.cuit.jobservice.Mapper.JobApplicationMapper;
import cn.cuit.jobservice.Mapper.JobPostMapper;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JobServiceImpl implements JobService {
    @Autowired
    private JobPostMapper jobPostMapper;
    @Autowired
    private JobApplicationMapper applicationMapper;

    public int publish(JobPost post) {
        return jobPostMapper.insert(post);
    }

    public List<JobPost> listByEmployer(Long employerId) {
        return jobPostMapper.findByEmployerId(employerId);
    }

    public List<JobPost> searchJobs(String keyword) {
        return jobPostMapper.search(keyword);
    }

    public int deleteById(Long id) {return jobPostMapper.deleteById(id);}

    @Override
    public JobPost getById(Long id) {
        return jobPostMapper.findById(id);
    }

    @Override
    public int update(JobPost post) {
        post.setUpdatedTime(new Date()); // 更新时间
        return jobPostMapper.update(post);
    }


    @Override
    public List<JobPost> getAllOpenJobs() {
        return jobPostMapper.findAllOpenJobs();
    }

    @Override
    public JobPost getJobDetail(Long jobId) {
        return jobPostMapper.findById(jobId);
    }

    public Map<String, Object> getJobStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalJobs", jobPostMapper.countAll());
        stats.put("activeJobs", jobPostMapper.countByStatus("OPEN"));
        stats.put("closedJobs", jobPostMapper.countByStatus("CLOSED"));
        stats.put("totalApplications", applicationMapper.countAll());
        stats.put("pendingApplications", applicationMapper.countByStatus("PENDING"));
        stats.put("acceptedApplications", applicationMapper.countByStatus("ACCEPTED"));
        stats.put("rejectedApplications", applicationMapper.countByStatus("REJECTED"));
        return stats;
    }

    @Override
    public Map<String, Object> deleteJob(Long jobId) {
        int rows = jobPostMapper.deleteById(jobId);
        Map<String, Object> result = new HashMap<>();
        result.put("success", rows > 0);
        result.put("message", rows > 0 ? "删除成功" : "删除失败或不存在");
        return result;
    }

    @Override
    public boolean updateApplicationStatus(Long id, String status) {
        return applicationMapper.updateStatus(id, status) > 0;
    }


    @Override
    public boolean updateJobStatus(Long id, String status) {
        // 简单校验一下状态值
        if (!"OPEN".equalsIgnoreCase(status) && !"CLOSE".equalsIgnoreCase(status)) {
            throw new IllegalArgumentException("状态值必须为 OPEN 或 CLOSE");
        }

        return jobPostMapper.updateJobStatus(id, status.toUpperCase()) > 0;
    }
}

