package com.paperai.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.paperai.common.BizException;
import com.paperai.common.JwtUtil;
import com.paperai.entity.User;
import com.paperai.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户服务：注册、登录
 */
@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    @Value("${paper-ai.jwt-secret}")
    private String jwtSecret;

    @Value("${paper-ai.jwt-expire-hours}")
    private long jwtExpireHours;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    /**
     * 注册
     */
    public void register(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new BizException(400, "用户名不能为空");
        }
        if (password == null || password.length() < 6) {
            throw new BizException(400, "密码长度不能少于 6 位");
        }
        String name = username.trim();
        Long count = userMapper.selectCount(
                new LambdaQueryWrapper<User>().eq(User::getUsername, name));
        if (count != null && count > 0) {
            throw new BizException(400, "用户名已存在");
        }
        User user = new User();
        user.setUsername(name);
        user.setPassword(encoder.encode(password));
        userMapper.insert(user);
    }

    /**
     * 登录，返回 token 与用户名
     */
    public Map<String, Object> login(String username, String password) {
        if (username == null || password == null) {
            throw new BizException(400, "用户名或密码错误");
        }
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getUsername, username.trim()));
        if (user == null || !encoder.matches(password, user.getPassword())) {
            throw new BizException(400, "用户名或密码错误");
        }
        String token = JwtUtil.createToken(user.getId(), user.getUsername(), jwtSecret, jwtExpireHours);
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("username", user.getUsername());
        return data;
    }
}
