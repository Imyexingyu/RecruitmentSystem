package cn.cuit.userservice.Controller;


import cn.cuit.userservice.Entity.UserCandidate;
import cn.cuit.userservice.Entity.UserEmployer;
import cn.cuit.userservice.Dao.UserInitDTO;
import cn.cuit.userservice.Service.UserCandidateService;
import cn.cuit.userservice.Service.UserEmployerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/init")
public class UserInitController {

    @Autowired
    private UserCandidateService candidateService;

    @Autowired
    private UserEmployerService employerService;

    @PostMapping("/candidate")
    public String initCandidate(@RequestBody UserInitDTO dto) {
        UserCandidate c = new UserCandidate();
        c.setUserId(dto.getUserId());
        candidateService.save(c);
        return "求职者初始化成功";
    }

    @PostMapping("/employer")
    public String initEmployer(@RequestBody UserInitDTO dto) {
        UserEmployer e = new UserEmployer();
        e.setUserId(dto.getUserId());
        employerService.save(e);
        return "企业用户初始化成功";
    }
}
