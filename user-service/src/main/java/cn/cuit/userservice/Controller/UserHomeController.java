package cn.cuit.userservice.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class UserHomeController {

    // 返回 HTML 页面（静态资源路径下的 home.html）
    @GetMapping("/user/home")
    public String userHome(HttpServletRequest request, Model model) {
        // 从请求头或session中获取用户名或用户ID（这里假设从请求头“username”）
        String username = request.getHeader("username");
        // 模拟查询用户信息，实际用Service调用数据库
        model.addAttribute("username", username);
        return "home";  // 返回 templates/user-home.html
    }

    @GetMapping("/user/company")
    public String userCompany(HttpServletRequest request, Model model) {
        // 从请求头或session中获取用户名或用户ID（这里假设从请求头“username”）
        String username = request.getHeader("username");
        // 模拟查询用户信息，实际用Service调用数据库
        model.addAttribute("username", username);
        return "company";  // 返回 templates/user-home.html
    }
}
