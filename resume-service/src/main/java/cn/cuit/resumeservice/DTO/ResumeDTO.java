package cn.cuit.resumeservice.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ResumeDTO {
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
    private List<EducationDTO> educations;
    private List<WorkExperienceDTO> workExperiences;
    private List<CampusExperienceDTO> campusExperiences;
    private List<SkillCertificateDTO> skillCertificates;
}



