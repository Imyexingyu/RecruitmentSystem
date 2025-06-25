package cn.cuit.authservice.util;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    private final String secret = "recruit-secret";
    private final long expire = 1000 * 60 * 60 * 2; // 2小时


    public String generateToken(String s,Long id, String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("sid", s)
                .claim("id", id)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expire))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }
    // 获取 token 剩余有效时间（毫秒）
    public long getExpire(String token) {
        Claims claims = parseToken(token);
        Date expiration = claims.getExpiration();
        return expiration.getTime() - System.currentTimeMillis();
    }

}
