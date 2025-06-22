package cn.cuit.userservice.Controller;

import cn.cuit.userservice.Entity.UserEmployer;
import cn.cuit.userservice.Service.UserEmployerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserEmployerController {

    @Autowired
    private UserEmployerService employerService;

    @GetMapping("/companyInfo")
    public UserEmployer getEmployerInfo(HttpServletRequest request) {
        Long id = Long.valueOf((request.getHeader("user_id")));
        return employerService.getByUserId(id);
    }

    @PostMapping("/companySave")
    public String saveEmployerInfo(@RequestBody UserEmployer employer) {
        int result = employerService.save(employer);
        return result > 0 ? "保存成功" : "保存失败";
    }

    @PostMapping("/companyUpdate")
    public String updateEmployerInfo(@RequestBody UserEmployer employer) {
        int result = employerService.update(employer);
        return result > 0 ? "更新成功" : "更新失败";
    }
}
