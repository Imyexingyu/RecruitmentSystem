package cn.cuit.resumeservice.Service;

import cn.cuit.resumeservice.DTO.*;
import cn.cuit.resumeservice.Entity.*;
import cn.cuit.resumeservice.Mapper.PersonalInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ResumeServiceImpl implements ResumeService {

    @Autowired
    private PersonalInfoMapper personalInfoMapper;

    private final String uploadDir = "gateway-service/src/main/resources/uploads/";

    @Override
    @Transactional
    public PersonalInfo createResume(ResumeDTO resumeDTO) {
        PersonalInfo personalInfo = new PersonalInfo();
        mapResumeDTOToEntity(resumeDTO, personalInfo);
        personalInfoMapper.insertPersonalInfo(personalInfo);

        if (personalInfo.getEducations() != null) {
            personalInfo.getEducations().forEach(personalInfoMapper::insertEducation);
        }
        if (personalInfo.getWorkExperiences() != null) {
            personalInfo.getWorkExperiences().forEach(personalInfoMapper::insertWorkExperience);
        }
        if (personalInfo.getCampusExperiences() != null) {
            personalInfo.getCampusExperiences().forEach(personalInfoMapper::insertCampusExperience);
        }
        if (personalInfo.getSkillCertificates() != null) {
            personalInfo.getSkillCertificates().forEach(personalInfoMapper::insertSkillCertificate);
        }

        return personalInfo;
    }

    @Override
    @Transactional
    public PersonalInfo updateResume(Long id, ResumeDTO resumeDTO) {
        PersonalInfo personalInfo = personalInfoMapper.findById(id);
        if (personalInfo == null) {
            throw new IllegalArgumentException("简历不存在，ID: " + id);
        }

        personalInfoMapper.deleteEducationsByPersonalId(id);
        personalInfoMapper.deleteWorkExperiencesByPersonalId(id);
        personalInfoMapper.deleteCampusExperiencesByPersonalId(id);
        personalInfoMapper.deleteSkillCertificatesByPersonalId(id);

        mapResumeDTOToEntity(resumeDTO, personalInfo);
        personalInfoMapper.updatePersonalInfo(personalInfo);

        if (personalInfo.getEducations() != null) {
            personalInfo.getEducations().forEach(personalInfoMapper::insertEducation);
        }
        if (personalInfo.getWorkExperiences() != null) {
            personalInfo.getWorkExperiences().forEach(personalInfoMapper::insertWorkExperience);
        }
        if (personalInfo.getCampusExperiences() != null) {
            personalInfo.getCampusExperiences().forEach(personalInfoMapper::insertCampusExperience);
        }
        if (personalInfo.getSkillCertificates() != null) {
            personalInfo.getSkillCertificates().forEach(personalInfoMapper::insertSkillCertificate);
        }

        return personalInfo;
    }

    @Override
    @Transactional
    public void deleteResume(Long id) {
        PersonalInfo personalInfo = personalInfoMapper.findById(id);
        if (personalInfo == null) {
            throw new IllegalArgumentException("简历不存在，ID: " + id);
        }
        personalInfoMapper.deleteEducationsByPersonalId(id);
        personalInfoMapper.deleteWorkExperiencesByPersonalId(id);
        personalInfoMapper.deleteCampusExperiencesByPersonalId(id);
        personalInfoMapper.deleteSkillCertificatesByPersonalId(id);
        personalInfoMapper.deletePersonalInfo(id);
    }


    @Override
    public ResumeDTO getFullResumeByUserId(Integer userId) {
        // 查询个人信息
        PersonalInfo personalInfo = personalInfoMapper.findByUserId(userId);
        if (personalInfo == null) {
            return null;  // 或抛异常、返回空对象，根据需求
        }

        // 查询所有关联信息
        List<Education> educations = personalInfoMapper.findEducationsByPersonalId(personalInfo.getId());
        List<WorkExperience> workExperiences = personalInfoMapper.findWorkExperiencesByPersonalId(personalInfo.getId());
        List<CampusExperience> campusExperiences = personalInfoMapper.findCampusExperiencesByPersonalId(personalInfo.getId());
        List<SkillCertificate> skillCertificates = personalInfoMapper.findSkillCertificatesByPersonalId(personalInfo.getId());

        // 组装成 DTO 对象返回
        ResumeDTO resumeDTO = new ResumeDTO();
        // 复制个人信息字段（假设你有工具方法或手写）
        copyPersonalInfoToDTO(personalInfo, resumeDTO);

        // 转换并设置各个列表
        resumeDTO.setEducations(convertToEducationDTOList(educations));
        resumeDTO.setWorkExperiences(convertToWorkExperienceDTOList(workExperiences));
        resumeDTO.setCampusExperiences(convertToCampusExperienceDTOList(campusExperiences));
        resumeDTO.setSkillCertificates(convertToSkillCertificateDTOList(skillCertificates));

        return resumeDTO;
    }

    // 复制个人信息字段到 ResumeDTO
    private void copyPersonalInfoToDTO(PersonalInfo personalInfo, ResumeDTO resumeDTO) {
        resumeDTO.setId(personalInfo.getId());
        resumeDTO.setUserId(personalInfo.getUserId());
        resumeDTO.setName(personalInfo.getName());
        resumeDTO.setGender(personalInfo.getGender());
        resumeDTO.setBirthDate(personalInfo.getBirthDate());
        resumeDTO.setPhone(personalInfo.getPhone());
        resumeDTO.setEmail(personalInfo.getEmail());
        resumeDTO.setAddress(personalInfo.getAddress());
        resumeDTO.setAvatarPath(personalInfo.getAvatarPath());
        resumeDTO.setSelfIntroduction(personalInfo.getSelfIntroduction());
        resumeDTO.setResumeFilePath(personalInfo.getResumeFilePath());
    }

    // 教育经历实体转DTO
    private List<EducationDTO> convertToEducationDTOList(List<Education> educations) {
        return educations.stream().map(e -> {
            EducationDTO dto = new EducationDTO();
            dto.setSchoolName(e.getSchoolName());
            dto.setMajor(e.getMajor());
            dto.setDegree(e.getDegree());
            dto.setStartDate(e.getStartDate());
            dto.setEndDate(e.getEndDate());
            dto.setDescription(e.getDescription());
            return dto;
        }).collect(Collectors.toList());
    }

    // 工作经历实体转DTO
    private List<WorkExperienceDTO> convertToWorkExperienceDTOList(List<WorkExperience> workExperiences) {
        return workExperiences.stream().map(w -> {
            WorkExperienceDTO dto = new WorkExperienceDTO();
            dto.setCompanyName(w.getCompanyName());
            dto.setPosition(w.getPosition());
            dto.setStartDate(w.getStartDate());
            dto.setEndDate(w.getEndDate());
            dto.setDescription(w.getDescription());
            return dto;
        }).collect(Collectors.toList());
    }

    // 校园经历实体转DTO
    private List<CampusExperienceDTO> convertToCampusExperienceDTOList(List<CampusExperience> campusExperiences) {
        return campusExperiences.stream().map(c -> {
            CampusExperienceDTO dto = new CampusExperienceDTO();
            dto.setActivityName(c.getActivityName());
            dto.setRole(c.getRole());
            dto.setStartDate(c.getStartDate());
            dto.setEndDate(c.getEndDate());
            dto.setDescription(c.getDescription());
            return dto;
        }).collect(Collectors.toList());
    }

    // 技能证书实体转DTO
    private List<SkillCertificateDTO> convertToSkillCertificateDTOList(List<SkillCertificate> skillCertificates) {
        return skillCertificates.stream().map(s -> {
            SkillCertificateDTO dto = new SkillCertificateDTO();
            dto.setSkillName(s.getSkillName());
            dto.setProficiency(s.getProficiency());
            dto.setCertificateName(s.getCertificateName());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public String uploadResumeFile(Long id, MultipartFile file) throws IOException {
        PersonalInfo personalInfo = personalInfoMapper.findById(id);
        if (personalInfo == null) {
            throw new IllegalArgumentException("简历不存在，ID: " + id);
        }

        String contentType = file.getContentType();
        if (!isValidFileType(contentType)) {
            throw new IllegalArgumentException("文件类型无效，仅支持 PDF 和 Word 文件");
        }

        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);

        Files.write(filePath, file.getBytes());

        personalInfo.setResumeFilePath(fileName);
        personalInfoMapper.updatePersonalInfo(personalInfo);

        return fileName;
    }

    @Override
    public Integer getUserIdByResumeId(Long resumeId) {
        return personalInfoMapper.getUserIdByResumeId(resumeId);
    }

    @Override
    public String getAvatarPath(Long id) {
        PersonalInfo personalInfo = personalInfoMapper.findById(id);
        return personalInfo != null ? personalInfo.getAvatarPath() : null;
    }

    @Override
    public String uploadAvatar(Long id, MultipartFile file) throws IOException {
        PersonalInfo personalInfo = personalInfoMapper.findById(id);
        if (personalInfo == null) {
            throw new IllegalArgumentException("简历不存在，ID: " + id);
        }

        // 验证文件类型（仅支持图片）
        String contentType = file.getContentType();
        if (!isValidImageType(contentType)) {
            throw new IllegalArgumentException("文件类型无效，仅支持 JPG、PNG、JPEG 文件");
        }

        // 创建上传目录
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 生成唯一文件名
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);

        // 保存文件
        Files.write(filePath, file.getBytes());

        // 更新 avatar_path
        personalInfo.setAvatarPath(fileName);
        personalInfoMapper.updatePersonalInfo(personalInfo);

        return filePath.toString();
    }

    private boolean isValidFileType(String contentType) {
        return contentType != null && (
                contentType.equals("application/pdf") ||
                        contentType.equals("application/msword") ||
                        contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
        );
    }

    private boolean isValidImageType(String contentType) {
        return contentType != null && (
                contentType.equals("image/jpeg") ||
                        contentType.equals("image/png") ||
                        contentType.equals("image/jpg")
        );
    }

    private void mapResumeDTOToEntity(ResumeDTO dto, PersonalInfo entity) {
        entity.setUserId(dto.getUserId());
        entity.setName(dto.getName());
        entity.setGender(dto.getGender());
        entity.setBirthDate(dto.getBirthDate());
        entity.setPhone(dto.getPhone());
        entity.setEmail(dto.getEmail());
        entity.setAddress(dto.getAddress());
        entity.setAvatarPath(dto.getAvatarPath());
        entity.setSelfIntroduction(dto.getSelfIntroduction());
        entity.setResumeFilePath(dto.getResumeFilePath());

        List<Education> educations = new ArrayList<>();
        if (dto.getEducations() != null) {
            for (EducationDTO eduDTO : dto.getEducations()) {
                Education education = new Education();
                education.setPersonalInfo(entity);
                education.setSchoolName(eduDTO.getSchoolName());
                education.setMajor(eduDTO.getMajor());
                education.setDegree(eduDTO.getDegree());
                education.setStartDate(eduDTO.getStartDate());
                education.setEndDate(eduDTO.getEndDate());
                education.setDescription(eduDTO.getDescription());
                educations.add(education);
            }
        }
        entity.setEducations(educations);

        List<WorkExperience> workExperiences = new ArrayList<>();
        if (dto.getWorkExperiences() != null) {
            for (WorkExperienceDTO workDTO : dto.getWorkExperiences()) {
                WorkExperience work = new WorkExperience();
                work.setPersonalInfo(entity);
                work.setCompanyName(workDTO.getCompanyName());
                work.setPosition(workDTO.getPosition());
                work.setStartDate(workDTO.getStartDate());
                work.setEndDate(workDTO.getEndDate());
                work.setDescription(workDTO.getDescription());
                workExperiences.add(work);
            }
        }
        entity.setWorkExperiences(workExperiences);

        List<CampusExperience> campusExperiences = new ArrayList<>();
        if (dto.getCampusExperiences() != null) {
            for (CampusExperienceDTO campusDTO : dto.getCampusExperiences()) {
                CampusExperience campus = new CampusExperience();
                campus.setPersonalInfo(entity);
                campus.setActivityName(campusDTO.getActivityName());
                campus.setRole(campusDTO.getRole());
                campus.setStartDate(campusDTO.getStartDate());
                campus.setEndDate(campusDTO.getEndDate());
                campus.setDescription(campusDTO.getDescription());
                campusExperiences.add(campus);
            }
        }
        entity.setCampusExperiences(campusExperiences);

        List<SkillCertificate> skillCertificates = new ArrayList<>();
        if (dto.getSkillCertificates() != null) {
            for (SkillCertificateDTO skillDTO : dto.getSkillCertificates()) {
                SkillCertificate skill = new SkillCertificate();
                skill.setPersonalInfo(entity);
                skill.setSkillName(skillDTO.getSkillName());
                skill.setProficiency(skillDTO.getProficiency());
                skill.setCertificateName(skillDTO.getCertificateName());
                skillCertificates.add(skill);
            }
        }
        entity.setSkillCertificates(skillCertificates);
    }

}