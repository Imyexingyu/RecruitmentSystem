package cn.cuit.resumeservice.Entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Data
public class Education {
    private Long id;
    @ManyToOne
    @JoinColumn(name = "personal_id")
    @JsonBackReference
    private PersonalInfo personalInfo;
    private String schoolName;
    private String major;
    private String degree;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
}