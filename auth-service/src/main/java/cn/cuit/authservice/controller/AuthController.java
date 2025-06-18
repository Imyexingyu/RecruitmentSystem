package cn.cuit.authservice.controller;


import cn.cuit.authservice.Dao.UserInitDTO;
import cn.cuit.authservice.Feign.RemoteUserClient;
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
    private final RemoteUserClient remoteUserClient;
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
        userMapper.save(user); // 保存用户

        Long userId = user.getId();
        if (userId == null) {
            // 如果id没回填，则重新查询获取ID
            User savedUser = userMapper.findByUsername(user.getUsername());
            if (savedUser == null) {
                result.put("code", 500);
                result.put("msg", "注册失败，无法获取用户ID");
                return result;
            }
            userId = savedUser.getId();
        }

        UserInitDTO dto = new UserInitDTO();
        dto.setUserId(userId);
        dto.setUsername(user.getUsername());

        try {
            if ("candidate".equalsIgnoreCase(user.getRole())) {
                remoteUserClient.initCandidate(dto);
            } else if ("employer".equalsIgnoreCase(user.getRole())) {
                remoteUserClient.initEmployer(dto);
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "注册成功，但同步 user-service 失败：" + e.getMessage());
            return result;
        }

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
        redisTemplate.opsForValue().set("TOKEN:" + token, "1", 2, TimeUnit.HOURS);

        result.put("code", 200);
        result.put("msg", "登录成功");
        result.put("token", token);
        result.put("role", user.getRole());
        return result;
    }
}


