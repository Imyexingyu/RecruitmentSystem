package cn.cuit.userservice.Controller;

import cn.cuit.userservice.Entity.UserEmployer;
import cn.cuit.userservice.Service.UserEmployerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employer")
public class UserEmployerController {

    @Autowired
    private UserEmployerService employerService;

    @GetMapping("/{userId}")
    public UserEmployer getEmployerInfo(@PathVariable Long userId) {
        return employerService.getByUserId(userId);
    }

    @PostMapping("/save")
    public String saveEmployerInfo(@RequestBody UserEmployer employer) {
        int result = employerService.save(employer);
        return result > 0 ? "保存成功" : "保存失败";
    }

    @PostMapping("/update")
    public String updateEmployerInfo(@RequestBody UserEmployer employer) {
        int result = employerService.update(employer);
        return result > 0 ? "更新成功" : "更新失败";
    }
}
