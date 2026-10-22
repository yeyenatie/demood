package com.example.demo.controller;

//用户控制器，接受前端请求，调用service,返回结果

import com.example.demo.dto.UserDTO;
import com.example.demo.service.UserService;
import com.example.demo.util.JwtUtil;
import com.example.demo.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Tag(name = "用户管理接口")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    @Operation(summary = "用户注册")
    public UserVO register(@Valid @RequestBody UserDTO userDTO) {
        return userService.register(userDTO);
    }

    @PostMapping("/login")
    @Operation(summary = "用户登录,返回JWT令牌")
    public Map<String, Object> login(@Valid @RequestBody UserDTO userDTO) {
        UserVO userVo = userService.login(userDTO);
        String token = jwtUtil.generateToken(userVo.getId(), userVo.getUsername());
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userInfo", userVo);
        return result;
    }

    @PatchMapping("/save")
    @Operation(summary = "新增/修改用户")
    public String saveUser(@Valid @RequestBody UserDTO userDTO) {
        userService.saveUser(userDTO);
        return "操作成功";
    }

    @GetMapping("/list")
    @Operation(summary = "获取用户列表")
    public List<UserVO> getUserList() {
        return userService.getUserList();
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除用户")
    public String deleteUser(@PathVariable Long id) {
        userService.removeById(id);
        return "删除成功";
    }

}
