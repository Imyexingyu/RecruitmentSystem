package cn.cuit.userservice.Controller;

import cn.cuit.userservice.Entity.UserProfile;
import cn.cuit.userservice.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/info")
    public Map<String, Object> getUserInfo(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        String username = request.getHeader("username");
        if (username == null) {
            result.put("code", 401);
            result.put("msg", "未认证用户");
            return result;
        }
        UserProfile profile = userService.findProfileByUsername(username);
        result.put("code", 200);
        result.put("msg", "查询成功");
        result.put("data", profile);
        return result;
    }
}
