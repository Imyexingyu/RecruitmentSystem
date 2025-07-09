package cn.cuit.jobservice.Controller;

import cn.cuit.jobservice.Entity.JobApplication;
import cn.cuit.jobservice.Mapper.JobApplicationMapper;
import cn.cuit.jobservice.Service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/jobs")
public class ApplicationController {

    @Autowired
    private JobApplicationMapper applicationMapper;
    @Autowired
    private JobService applicationService;
    @PostMapping("/apply")
    public String apply(@RequestBody JobApplication application, HttpServletRequest request) {
        List<JobApplication> existing = applicationMapper.checkDuplicate(application.getCandidateId(), application.getJobId());
        Integer userId = Integer.valueOf(request.getHeader("user_id"));
        application.setCandidateId(Long.valueOf(userId));
        if (!existing.isEmpty()) {
            return "您已投递过该职位，请勿重复投递";
        }
        return applicationMapper.apply(application) > 0 ? "投递成功" : "投递失败";
    }

    @GetMapping("/job/{jobId}")
    public List<JobApplication> getByJob(@PathVariable Long jobId) {
        return applicationMapper.findByJobId(jobId);
    }

    @GetMapping("/jobseeker/{id}")
    public List<JobApplication> getByCandidate(@PathVariable("id") Long candidateId) {
        return applicationMapper.findByCandidateId(candidateId);

    }

    @PutMapping("/application/{id}/status")
    public String updateApplicationStatus(@PathVariable Long id, @RequestParam String status) {
        boolean success = applicationService.updateApplicationStatus(id, status);
        return success ? "更新成功" : "更新失败";
    }
}
