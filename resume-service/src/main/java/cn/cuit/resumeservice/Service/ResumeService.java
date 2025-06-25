package cn.cuit.resumeservice.Service;

import cn.cuit.resumeservice.DTO.ResumeDTO;
import cn.cuit.resumeservice.Entity.PersonalInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ResumeService {

    PersonalInfo createResume(ResumeDTO resumeDTO);

    PersonalInfo updateResume(Long id, ResumeDTO resumeDTO);

    void deleteResume(Long id);

    String uploadResumeFile(Long id, MultipartFile file) throws IOException;

    String getAvatarPath(Long id);

    String uploadAvatar(Long id, MultipartFile file) throws IOException;

    Integer getUserIdByResumeId(Long resumeId) ;

    ResumeDTO getFullResumeByUserId(Integer userId);
}
