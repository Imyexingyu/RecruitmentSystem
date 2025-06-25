package cn.cuit.resumeservice.DTO;

import lombok.Data;


import java.time.LocalDate;

@Data
public class CampusExperienceDTO {
    private String activityName;
    private String role;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
}
