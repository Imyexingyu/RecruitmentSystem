package cn.cuit.userservice.Controller;

import cn.cuit.userservice.Entity.UserCandidate;
import cn.cuit.userservice.Service.UserCandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserCandidateController {

    @Autowired
    private UserCandidateService candidateService;

    @GetMapping("/info")
    public UserCandidate getCandidateInfo(HttpServletRequest request) {
        Long id = Long.valueOf((request.getHeader("user_id")));
        return candidateService.getByUserId(id);
    }

    @PostMapping("/save")
    public String saveCandidateInfo(@RequestBody UserCandidate candidate) {
        int result = candidateService.save(candidate);
        return result > 0 ? "保存成功" : "保存失败";
    }

    @PostMapping("/update")
    public String updateCandidateInfo(@RequestBody UserCandidate candidate,@RequestHeader("user_id") Long userIdFromToken) {
        //强制设置user_id为登录用户的id防止越权访问
        candidate.setUser_id(userIdFromToken);
        int result = candidateService.update(candidate);
        return result > 0 ? "更新成功" : "更新失败";
    }

    @GetMapping("/exists/{id}")
    public Map<String, Object> existsCandidate(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        UserCandidate candidate = candidateService.getByUserId(id);
        boolean exists = candidate != null;
        response.put("success", true);
        response.put("data", exists);
        return response;
    }
}
