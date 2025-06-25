package cn.cuit.authservice.controller;

import cn.cuit.authservice.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthUserController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/statistics")
    public Map<String, Object> getUserCounts() {
        Map<String, Object> result = new HashMap<>();
        result.put("totalUsers", userMapper.countAll());
        result.put("candidateUsers", userMapper.countByRole("jobseeker"));
        result.put("employerUsers", userMapper.countByRole("EMPLOYER"));
        result.put("blockedUsers", userMapper.countByStatus("BLOCKED"));
        return result;
    }
}
