package cn.cuit.authservice.service;

import cn.cuit.authservice.entity.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    Map<String, Object> getUserCounts();

    Map<String, Object> blockUser(Long userId);

    Map<String, Object> unblockUser(Long userId);

    Map<String, Object> deleteUser(Long userId);

    List<User> AllUsers();
}
