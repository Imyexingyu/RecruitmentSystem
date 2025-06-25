package cn.cuit.resumeservice.DTO;

import lombok.Data;


import java.time.LocalDate;

@Data
public class WorkExperienceDTO {
    private String companyName;
    private String position;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
}
