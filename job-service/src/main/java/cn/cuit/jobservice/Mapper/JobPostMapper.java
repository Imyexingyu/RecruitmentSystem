package cn.cuit.jobservice.Mapper;

import cn.cuit.jobservice.Entity.JobPost;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface JobPostMapper {

    @Insert("INSERT INTO job_post (employer_id, title, description, location, salary, requirements) VALUES (#{employerId}, #{title}, #{description}, #{location}, #{salary}, #{requirements})")
    int insert(JobPost jobPost);

    @Select("SELECT * FROM job_post WHERE employer_id = #{employerId}")
    List<JobPost> findByEmployerId(Long employerId);

    @Select("SELECT * FROM job_post WHERE status = 'OPEN' AND title LIKE CONCAT('%',#{keyword},'%')")
    List<JobPost> search(String keyword);
    @Delete("DELETE FROM job_post WHERE id = #{id}")
    int deleteById(Long id);

    @Select("SELECT * FROM job_post WHERE id = #{id}")
    JobPost findById(Long id);

    @Update("UPDATE job_post SET " +
            "title = #{title}, " +
            "location = #{location}, " +
            "salary = #{salary}, " +
            "requirements = #{requirements}, " +
            "description = #{description}, " +
            "updated_time = #{updatedTime} " +
            "WHERE id = #{id}")
    int update(JobPost post);
}

