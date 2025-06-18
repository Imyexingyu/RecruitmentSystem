package cn.cuit.userservice.Controller;

import cn.cuit.userservice.Entity.UserCandidate;
import cn.cuit.userservice.Service.UserCandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/candidate")
public class UserCandidateController {

    @Autowired
    private UserCandidateService candidateService;

    @GetMapping("/{userId}")
    public UserCandidate getCandidateInfo(@PathVariable Long userId) {
        return candidateService.getByUserId(userId);
    }

    @PostMapping("/save")
    public String saveCandidateInfo(@RequestBody UserCandidate candidate) {
        int result = candidateService.save(candidate);
        return result > 0 ? "保存成功" : "保存失败";
    }

    @PostMapping("/update")
    public String updateCandidateInfo(@RequestBody UserCandidate candidate) {
        int result = candidateService.update(candidate);
        return result > 0 ? "更新成功" : "更新失败";
    }
}
