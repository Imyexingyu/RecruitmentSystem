package cn.cuit.jobservice.Mapper;

import cn.cuit.jobservice.Entity.JobApplication;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface JobApplicationMapper {

    @Insert("INSERT INTO job_application (job_id, candidate_id, resume) VALUES (#{jobId}, #{candidateId}, #{resume})")
    int apply(JobApplication application);

    @Select("SELECT * FROM job_application WHERE job_id = #{jobId}")
    List<JobApplication> findByJobId(Long jobId);
}

