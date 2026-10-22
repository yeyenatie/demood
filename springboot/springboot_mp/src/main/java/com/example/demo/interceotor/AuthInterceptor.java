package com.example.demo.interceotor;

import com.example.demo.common.Result;
import com.example.demo.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Resource
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //预检请求直接放行，避免 OPTIONS 因无 Authorization 头被拦截
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String requestUri = request.getRequestURI();
        //只有下单接口需要验证
        if (!requestUri.startsWith("/order/create")) {
            return true;
        }
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(objectMapper.writeValueAsString(Result.fail(401, "请先登录")));
            return false;
        }
        String realToken = token.substring(7);
        if (!jwtUtil.validateToken(realToken)) {
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(objectMapper.writeValueAsString(Result.fail(401, "token已过期或无效")));
            return false;
        }
        Long loginUserId = jwtUtil.getUserId(realToken);
        request.setAttribute("loginUserId", loginUserId);
        return true;
    }
}