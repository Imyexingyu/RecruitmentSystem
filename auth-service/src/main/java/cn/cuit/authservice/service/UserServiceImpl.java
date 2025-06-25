package cn.cuit.authservice.service;

import cn.cuit.authservice.entity.User;
import cn.cuit.authservice.mapper.UserMapper;
import cn.cuit.authservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    private static final String STATUS_ACTIVE = "ACTIVE";
    private static final String STATUS_BLOCKED = "BLOCKED";

    private static final String ROLE_CANDIDATE = "jobseeker";
    private static final String ROLE_EMPLOYER = "EMPLOYER";

    @Autowired
    private UserMapper userMapper;

    @Override
    public Map<String, Object> getUserCounts() {
        Map<String, Object> counts = new HashMap<>();
        int totalUsers = userMapper.countAll();
        int candidateUsers = userMapper.countByRole(ROLE_CANDIDATE);
        int employerUsers = userMapper.countByRole(ROLE_EMPLOYER);
        int blockedUsers = userMapper.countByStatus(STATUS_BLOCKED);

        counts.put("totalUsers", totalUsers);
        counts.put("candidateUsers", candidateUsers);
        counts.put("employerUsers", employerUsers);
        counts.put("blockedUsers", blockedUsers);

        return counts;
    }

    @Override
    public Map<String, Object> blockUser(Long userId) {
        int rows = userMapper.updateStatus(userId, STATUS_BLOCKED);
        Map<String, Object> res = new HashMap<>();
        if (rows > 0) {
            res.put("success", true);
            res.put("message", "用户封禁成功");
        } else {
            res.put("success", false);
            res.put("message", "用户不存在");
        }
        return res;
    }

    @Override
    public Map<String, Object> unblockUser(Long userId) {
        int rows = userMapper.updateStatus(userId, STATUS_ACTIVE);
        Map<String, Object> res = new HashMap<>();
        if (rows > 0) {
            res.put("success", true);
            res.put("message", "用户解封成功");
        } else {
            res.put("success", false);
            res.put("message", "用户不存在");
        }
        return res;
    }

    @Override
    public Map<String, Object> deleteUser(Long userId) {
        int rows = userMapper.deleteById(userId);
        Map<String, Object> res = new HashMap<>();
        if (rows > 0) {
            res.put("success", true);
            res.put("message", "用户删除成功");
        } else {
            res.put("success", false);
            res.put("message", "用户不存在");
        }
        return res;
    }

    public List<User> AllUsers() {
        return userMapper.findAll();
    }
}
