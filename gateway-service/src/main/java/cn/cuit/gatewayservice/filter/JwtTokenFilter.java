package cn.cuit.gatewayservice.filter;



import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class JwtTokenFilter extends ZuulFilter {

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
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        // 放行注册登录接口
        String uri = request.getRequestURI();
        return !(uri.contains("/auth/login") || uri.contains("/auth/register"));
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            deny(ctx, 401, "Token 缺失");
            return null;
        }

        token = token.replace("Bearer ", "");
        try {
            // 校验 Redis 中是否存在
            if (redisTemplate.opsForValue().get("TOKEN:" + token) == null) {
                deny(ctx, 401, "Token 已失效或未登录");
                return null;
            }

            // 校验 JWT 合法性
            String secret = "recruit-secret";
            Claims claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();

            // 将用户信息传递下游服务
            ctx.addZuulRequestHeader("username", claims.getSubject());
            ctx.addZuulRequestHeader("role", claims.get("role").toString());

        } catch (Exception e) {
            deny(ctx, 403, "Token 非法");
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
