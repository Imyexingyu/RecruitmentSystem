package cn.cuit.jobservice.Mapper;

import cn.cuit.jobservice.Entity.JobApplication;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface JobApplicationMapper {

    @Insert("INSERT INTO job_application (job_id, candidate_id, resume) VALUES (#{jobId}, #{candidateId}, #{resume})")
    int apply(JobApplication application);

    @Select("SELECT * FROM job_application WHERE job_id = #{jobId}")
    List<JobApplication> findByJobId(Long jobId);

    @Select("SELECT * FROM job_application WHERE candidate_id = #{candidateId} AND job_id = #{jobId}")
    List<JobApplication> checkDuplicate(@Param("candidateId") Long candidateId, @Param("jobId") Long jobId);


    @Select("SELECT COUNT(*) FROM job_application")
    int countAll();

    @Select("SELECT COUNT(*) FROM job_application WHERE status = #{status}")
    int countByStatus(@Param("status") String status);


}

