package cn.cuit.adminservice.repository;

import cn.cuit.adminservice.entity.UserStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserStatisticsRepository extends JpaRepository<UserStatistics, Long> {
    
    UserStatistics findTopByOrderByLastUpdatedDesc();
} 