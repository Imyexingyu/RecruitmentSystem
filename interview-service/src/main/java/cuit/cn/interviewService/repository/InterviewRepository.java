package cuit.cn.interviewService.repository;

import cuit.cn.interviewService.model.Interview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long> {
    
    List<Interview> findByApplicationId(Long applicationId);
    
    Page<Interview> findByApplicationId(Long applicationId, Pageable pageable);
    
    Optional<Interview> findByIdAndApplicationId(Long id, Long applicationId);

    List<Interview> findByJobId(Long jobId);
    
    List<Interview> findByUserId(Long userId);
} 