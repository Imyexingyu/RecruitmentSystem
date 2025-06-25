package cn.cuit.jobservice.Controller;

import cn.cuit.jobservice.Entity.JobPost;
import cn.cuit.jobservice.Service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    @PostMapping("/publish")
    public String publishJob(@RequestBody JobPost post,@RequestHeader("user_id") Long userIdFromToken) {
        post.setEmployerId(userIdFromToken);//设定职位发布者id
        System.out.println(userIdFromToken);
        return jobService.publish(post) > 0 ? "发布成功" : "发布失败";
    }

    @GetMapping("/employer/{employerId}")
    public List<JobPost> listByEmployer(@PathVariable Long employerId) {
        return jobService.listByEmployer(employerId);
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> deleteJob(@PathVariable Long id) {
        boolean success = jobService.deleteById(id) > 0;
        Map<String, Object> result = new HashMap<>();
        result.put("code", success ? 200 : 500);
        result.put("msg", success ? "删除成功" : "删除失败");
        return result;
    }

    @GetMapping("/{id}")
    public JobPost getJobById(@PathVariable Long id) {
        return jobService.getById(id);
    }

    // 更新职位信息
    @PutMapping("/update")
    public String updateJob(@RequestBody JobPost post) {
        return jobService.update(post) > 0 ? "更新成功" : "更新失败";
    }

    @GetMapping("/search")
    public List<JobPost> search(@RequestParam String keyword) {
        return jobService.searchJobs(keyword);
    }

    @GetMapping("/all")
    public List<JobPost> listAllOpenJobs() {
        return jobService.getAllOpenJobs();
    }

    @GetMapping("/statistics")
    public Map<String, Object> getJobStatistics() {
        return jobService.getJobStatistics();
    }
}
