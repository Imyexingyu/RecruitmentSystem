package cn.cuit.resumeservice.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {
    @GetMapping({"/", "/resume"})
    public String showResumePage() {
        return "resume";
    }
    @GetMapping("/test")
    public String helloTest() {
        return "hello";
    }

}