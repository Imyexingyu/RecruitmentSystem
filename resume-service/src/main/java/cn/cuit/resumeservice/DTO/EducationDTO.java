package cn.cuit.resumeservice.DTO;

import lombok.Data;


import java.time.LocalDate;

@Data
public class EducationDTO {
    private String schoolName;
    private String major;
    private String degree;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
}