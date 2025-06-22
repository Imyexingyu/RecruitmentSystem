package cn.cuit.jobservice.Controller;

import cn.cuit.jobservice.Entity.JobApplication;
import cn.cuit.jobservice.Mapper.JobApplicationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobs")
public class ApplicationController {

    @Autowired
    private JobApplicationMapper applicationMapper;

    @PostMapping("/apply")
    public String apply(@RequestBody JobApplication application) {
        return applicationMapper.apply(application) > 0 ? "投递成功" : "投递失败";
    }

    @GetMapping("/job/{jobId}")
    public List<JobApplication> getByJob(@PathVariable Long jobId) {
        return applicationMapper.findByJobId(jobId);
    }
}
