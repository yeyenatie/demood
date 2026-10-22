package com.paperai.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paperai.common.JwtUtil;
import com.paperai.common.Result;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JWT 登录拦截器：校验 Authorization: Bearer xxx，并把 userId 放入请求属性
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Value("${paper-ai.jwt-secret}")
    private String secret;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 放行预检请求
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        String auth = request.getHeader("Authorization");
        try {
            if (auth == null || !auth.startsWith("Bearer ")) {
                writeUnauthorized(response);
                return false;
            }
            Claims claims = JwtUtil.parse(auth.substring(7), secret);
            request.setAttribute("userId", ((Number) claims.get("userId")).longValue());
            request.setAttribute("username", claims.getSubject());
            return true;
        } catch (Exception e) {
            writeUnauthorized(response);
            return false;
        }
    }

    private void writeUnauthorized(HttpServletResponse response) throws Exception {
        response.setStatus(200);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(Result.fail(401, "未登录或登录已过期")));
    }
}
