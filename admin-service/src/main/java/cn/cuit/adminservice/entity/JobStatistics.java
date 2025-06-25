package cn.cuit.adminservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "job_statistics")
public class JobStatistics {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Integer totalJobs;
    
    private Integer activeJobs;
    
    private Integer closedJobs;
    
    private Integer totalApplications;
    
    private Integer pendingApplications;
    
    private Integer acceptedApplications;
    
    private Integer rejectedApplications;
    
    private LocalDateTime lastUpdated;
} 