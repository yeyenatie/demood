package com.paperai.common;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

/**
 * JWT 工具类（jjwt 0.9.1，JDK8 兼容）
 */
public class JwtUtil {

    private JwtUtil() {
    }

    /**
     * 生成 token
     */
    public static String createToken(Long userId, String username, String secret, long expireHours) {
        Date now = new Date();
        Date expire = new Date(now.getTime() + expireHours * 3600_000L);
        return Jwts.builder()
                .claim("userId", userId)
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expire)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    /**
     * 解析 token，校验失败会抛出异常
     */
    public static Claims parse(String token, String secret) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }
}
