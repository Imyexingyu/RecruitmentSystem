package cn.cuit.adminservice.repository;

import cn.cuit.adminservice.entity.JobStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobStatisticsRepository extends JpaRepository<JobStatistics, Long> {
    
    JobStatistics findTopByOrderByLastUpdatedDesc();
} 