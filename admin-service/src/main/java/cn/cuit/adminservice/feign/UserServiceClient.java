package cn.cuit.adminservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@FeignClient(name = "auth-service")
public interface UserServiceClient {

    @GetMapping("/auth/statistics")
    Map<String, Object> getUserCounts();

    @PostMapping("/auth/{userId}/block")
    Map<String, Object> blockUser(@PathVariable("userId") Long userId);

    @PostMapping("/auth/{userId}/unblock")
    Map<String, Object> unblockUser(@PathVariable("userId") Long userId);

    @DeleteMapping("/auth/{userId}")
    Map<String, Object> deleteUser(@PathVariable("userId") Long userId);

}