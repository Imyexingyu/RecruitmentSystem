package cn.cuit.resumeservice.Controller;

import cn.cuit.resumeservice.Config.Result;
import cn.cuit.resumeservice.DTO.ResumeDTO;
import cn.cuit.resumeservice.Entity.PersonalInfo;
import cn.cuit.resumeservice.Service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/resume")
public class ResumeController {

    @Autowired
    private ResumeService resumeService;

    @PostMapping
    public ResponseEntity<Result<PersonalInfo>> createResume(@RequestBody ResumeDTO resumeDTO,HttpServletRequest request) {
//        if(!request.getHeader("role").equals("jobseeker")) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                    .body(Result.fail("无权修改该简历"));
//        }
        resumeDTO.setUserId(Integer.valueOf(request.getHeader("user_id")));
        PersonalInfo createdResume = resumeService.createResume(resumeDTO);
        return ResponseEntity.ok(Result.success(createdResume));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Result<PersonalInfo>> updateResume(@PathVariable Long id, @RequestBody ResumeDTO resumeDTO, HttpServletRequest request) {
        Integer userId = Integer.valueOf(request.getHeader("user_id"));

        PersonalInfo updatedResume = resumeService.updateResume(id, resumeDTO);
        return ResponseEntity.ok(Result.success(updatedResume));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Result<Void>> deleteResume(@PathVariable Long id,HttpServletRequest request) {
        Integer userId = Integer.valueOf(request.getHeader("user_id"));
        Integer ownerUserId = resumeService.getUserIdByResumeId(id);
//        if (!userId.equals(ownerUserId) && "jobseeker".equals(request.getHeader("role"))) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                    .body(Result.fail("无权修改该简历"));
//        }
        resumeService.deleteResume(id);
        return ResponseEntity.ok(Result.success());
    }

    @PostMapping("/{id}/upload")
    public ResponseEntity<Result<String>> uploadResumeFile(@PathVariable Long id, @RequestParam("file") MultipartFile file,HttpServletRequest request) throws IOException {
        Integer userId = Integer.valueOf(request.getHeader("user_id"));
        Integer ownerUserId = resumeService.getUserIdByResumeId(id);
//        if (!userId.equals(ownerUserId) && "employer".equals(request.getHeader("role"))) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                    .body(Result.fail("无权修改该简历"));
//        }
        String filePath = resumeService.uploadResumeFile(id, file);
        return ResponseEntity.ok(Result.success(filePath));
    }

    @GetMapping("/avatar/{id}")
    public String getAvatar(@PathVariable Long id) {
        return resumeService.getAvatarPath(id);
    }

    @PostMapping("/{id}/upload-avatar")
    public ResponseEntity<Result<String>> uploadAvatar(@PathVariable Long id, @RequestParam("file") MultipartFile file,HttpServletRequest request) throws IOException {
        Integer userId = Integer.valueOf(request.getHeader("user_id"));
        Integer ownerUserId = resumeService.getUserIdByResumeId(id);
//        if (!userId.equals(ownerUserId) && "employer".equals(request.getHeader("role"))){
//            return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                    .body(Result.fail("无权修改该简历"));
//        }
        String avatarPath = resumeService.uploadAvatar(id, file);
        return ResponseEntity.ok(Result.success(avatarPath));
    }

    @GetMapping("/my")
    public ResponseEntity<Result<ResumeDTO>> getMyResume(HttpServletRequest request) {
        Integer userId = Integer.valueOf(request.getHeader("user_id"));
        ResumeDTO resume = resumeService.getFullResumeByUserId(userId);
        if (resume != null) {
            return ResponseEntity.ok(Result.success(resume));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Result.fail("未找到该用户的简历"));
        }
    }
}