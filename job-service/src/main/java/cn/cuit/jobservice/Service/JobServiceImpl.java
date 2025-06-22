package cn.cuit.jobservice.Service;

import cn.cuit.jobservice.Entity.JobPost;
import cn.cuit.jobservice.Mapper.JobPostMapper;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class JobServiceImpl implements JobService {
    @Autowired
    private JobPostMapper jobPostMapper;

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
}

