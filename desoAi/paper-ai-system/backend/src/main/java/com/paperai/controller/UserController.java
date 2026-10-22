package com.paperai.controller;

import com.paperai.common.Result;
import com.paperai.dto.LoginRequest;
import com.paperai.dto.RegisterRequest;
import com.paperai.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 用户接口：注册、登录
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public Result<Void> register(@RequestBody RegisterRequest req) {
        userService.register(req.getUsername(), req.getPassword());
        return Result.ok();
    }

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody LoginRequest req) {
        return Result.ok(userService.login(req.getUsername(), req.getPassword()));
    }
}
