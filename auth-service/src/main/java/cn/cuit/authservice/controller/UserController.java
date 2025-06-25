package cn.cuit.authservice.controller;


import cn.cuit.authservice.entity.User;
import cn.cuit.authservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/{userId}/block")
    public Map<String, Object> blockUser(@PathVariable Long userId) {
        return userService.blockUser(userId);
    }

    @PostMapping("/{userId}/unblock")
    public Map<String, Object> unblockUser(@PathVariable Long userId) {
        return userService.unblockUser(userId);
    }

    @DeleteMapping("/{userId}")
    public Map<String, Object> deleteUser(@PathVariable Long userId) {
        return userService.deleteUser(userId);
    }

    @GetMapping("/allUser")
    public List<User> getAllUser() {
        return userService.AllUsers();
    }
}