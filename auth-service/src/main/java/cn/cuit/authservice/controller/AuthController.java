package cn.cuit.authservice.controller;


import cn.cuit.authservice.entity.User;
import cn.cuit.authservice.mapper.UserMapper;
import cn.cuit.authservice.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    private StringRedisTemplate redisTemplate;

    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody User user) {
        Map<String, Object> result = new HashMap<>();
        if (userMapper.findByUsername(user.getUsername()) != null) {
            result.put("code", 400);
            result.put("msg", "用户名已存在");
            return result;
        }
        user.setPassword(encoder.encode(user.getPassword()));
        userMapper.save(user);
        result.put("code", 200);
        result.put("msg", "注册成功");
        return result;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody User input) {
        User user = userMapper.findByUsername(input.getUsername());
        Map<String, Object> result = new HashMap<>();
        if (user == null || !encoder.matches(input.getPassword(), user.getPassword())) {
            result.put("code", 401);
            result.put("msg", "用户名或密码错误");
            return result;
        }
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
        // 写入 Redis，2小时过期（单位换成 TimeUnit.HOURS）
        redisTemplate.opsForValue().set("TOKEN:" + token, "1", 2, TimeUnit.HOURS);

        result.put("code", 200);
        result.put("msg", "登录成功");
        result.put("token", token);
        result.put("role", user.getRole());
        return result;
    }
}

