package cn.cuit.resumeservice.Entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


@Data
public class SkillCertificate {
    private Long id;
    @ManyToOne
    @JoinColumn(name = "personal_id")
    @JsonBackReference
    private PersonalInfo personalInfo;
    private String skillName;
    private String proficiency;
    private String certificateName;
}