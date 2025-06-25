package cn.cuit.resumeservice.Entity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.List;
@Data
public class PersonalInfo {
    private Long id;
    private Integer userId;
    private String name;
    private String gender;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate birthDate;
    private String phone;
    private String email;
    private String address;
    private String avatarPath;
    private String selfIntroduction;
    private String resumeFilePath;
    @OneToMany(mappedBy = "personalInfo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Education> educations;
    @OneToMany(mappedBy = "personalInfo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<WorkExperience> workExperiences;
    @OneToMany(mappedBy = "personalInfo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<CampusExperience> campusExperiences;
    @OneToMany(mappedBy = "personalInfo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<SkillCertificate> skillCertificates;
}
