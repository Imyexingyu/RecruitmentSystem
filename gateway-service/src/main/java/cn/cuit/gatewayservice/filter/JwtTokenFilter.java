package cn.cuit.gatewayservice.filter;



import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Component
public class JwtTokenFilter extends ZuulFilter {

    private final String secret = "recruit-secret";

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public String filterType() {
        return "pre"; // 请求前过滤
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
        String path = request.getRequestURI();

        // 打印日志确认路径
        System.out.println("Zuul 请求路径：" + path);
        String token = request.getHeader("Authorization");
        System.out.println(token);
        // 明确放行 auth 登录、注册接口
        if (path.startsWith("/auth/login") || path.startsWith("/auth/register")) {
            System.out.println("放行路径：" + path);
            return false;
        }

        return true;
    }


    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        String token = request.getHeader("Authorization");

// ✅ 从 Cookie 读取
        if (token == null) {
            if (request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                    if ("token".equals(cookie.getName())) {
                        token = "Bearer " + cookie.getValue();
                        break;
                    }
                }
            }
        }
        if (token == null || !token.startsWith("Bearer ")) {
            System.out.println(token);
            deny(ctx, 401, "你没有登录");
            return null;
        }

        token = token.replace("Bearer ", "");

        try {
            // 检查 Redis 中是否存在
            if (redisTemplate.opsForValue().get("TOKEN:" + token) == null) {
                deny(ctx, 401, "登录过期/你没有登录");
                return null;
            }

            Claims claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
            ctx.addZuulRequestHeader("X-Gateway-Auth", "recruit-gateway-secret");
            ctx.addZuulRequestHeader("username", claims.getSubject());
            System.out.println(claims.getSubject());
            ctx.addZuulRequestHeader("role", claims.get("role").toString());
            System.out.println(claims.get("role"));
            ctx.addZuulRequestHeader("user_id", claims.get("id").toString());
            System.out.println(claims.get("user_id"));

        } catch (Exception e) {
            deny(ctx, 403, "非法登录");
        }

        return null;
    }
    private void deny(RequestContext ctx, int status, String msg) {
        ctx.setSendZuulResponse(false);
        ctx.setResponseStatusCode(status);
        ctx.setResponseBody("{\"msg\":\"" + msg + "\"}");
        ctx.getResponse().setContentType("application/json;charset=UTF-8");
    }
}
