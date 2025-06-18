package cn.cuit.gatewayservice.Config;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable() // 禁用 CSRF
                .authorizeRequests()
                .antMatchers("/auth/login", "/auth/register","/auth/auth/login","/login","/index.html").permitAll() // 放行
                .anyRequest().authenticated(); // 其他请求都要认证
    }
}



