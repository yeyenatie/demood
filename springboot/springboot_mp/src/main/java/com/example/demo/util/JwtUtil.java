package com.example.demo.util;

//JWT工具类:生成token，解析token，验证token的正确性

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {
    private final RestClient.Builder builder;
    @Value("${jwt.secret}")
    private String secretStr;
    @Value("${jwt.expire}")
    private Long expire;
    private SecretKey secretKey;

    public JwtUtil(RestClient.Builder builder) {
        this.builder = builder;
    }

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretStr);
        secretKey = Keys.hmacShaKeyFor(bytes);
    }

    //生成JWT令牌
    public String generateToken(Long userId, String username) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + expire);
        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    //从token获取用户id
    public Long getUserId(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
        return claims.get("userId", Long.class);
    }

    //从token获取用户名
    public String getUsername(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).
                build().parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    //校验token是否有效
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).
                    build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
        } catch (MalformedJwtException e) {
        } catch (SignatureException e) {
        } catch (Exception e) {
        }
        return false;
    }
}

