package cuit.cn.interviewService.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Configuration
public class FeignConfig {
    
    @Bean
    public Retryer feignRetryer() {
        // 重试间隔为100ms，最大重试间隔为1s，最多重试3次
        return new Retryer.Default(100, 1000, 3);
    }
    
    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                // 始终使用服务角色进行微服务之间的调用
                template.header("username", "interview-service");
                template.header("role", "SERVICE");
                
                // 尝试从当前请求中获取其他相关头部
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                if (attributes != null) {
                    HttpServletRequest request = attributes.getRequest();
                    
                    // 传递其他可能有用的头部
                    String userId = request.getHeader("user_id");
                    String authorization = request.getHeader("Authorization");
                    
                    if (userId != null) {
                        template.header("user_id", userId);
                    }
                    
                    if (authorization != null) {
                        template.header("Authorization", authorization);
                    }
                }
            }
        };
    }
} 