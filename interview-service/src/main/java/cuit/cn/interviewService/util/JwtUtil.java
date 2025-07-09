package cuit.cn.interviewService.util;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    private final String secret = "recruit-secret";
    private final long expire = 1000 * 60 * 60 * 2; // 2小时

    public Claims parseToken(String token) {
        try {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            return null;
        }
    }
    
    public Long getUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        if (claims != null) {
            return Long.valueOf(claims.get("id").toString());
        }
        return null;
    }
    
    public String getUsernameFromToken(String token) {
        Claims claims = parseToken(token);
        if (claims != null) {
            return claims.getSubject();
        }
        return null;
    }
    
    public String getRoleFromToken(String token) {
        Claims claims = parseToken(token);
        if (claims != null) {
            return claims.get("role", String.class);
        }
        return null;
    }
    
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            // 签名验证失败
            return false;
        } catch (MalformedJwtException e) {
            // 令牌格式错误
            return false;
        } catch (ExpiredJwtException e) {
            // 令牌已过期
            return false;
        } catch (UnsupportedJwtException e) {
            // 不支持的令牌
            return false;
        } catch (IllegalArgumentException e) {
            // 令牌为空
            return false;
        }
    }
} 