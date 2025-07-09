package cuit.cn.interviewService.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobDTO {
    private Long id;
    private Long employerId;
    private String title;
    private String description;
    private String requirements;
    private String location;
    private String salary;
    private String status;
    private String createdAt;
    private String updatedAt;
} 