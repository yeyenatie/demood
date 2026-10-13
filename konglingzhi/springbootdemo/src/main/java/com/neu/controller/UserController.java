package com.neu.controller;

import com.neu.entity.User;
import com.neu.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserMapper userMapper;

    //    查询全部用户
    @GetMapping("/list")
    public List<User> getUserList() {
        List<User> list = userMapper.selectAll();
        return list;
    }

    //    根据id查询
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Integer id) {
        User user = userMapper.selectById(id);
        return user;
    }

    //    按账号模糊查询
    //    localhost:8080/user/search?username=xxx
    @GetMapping("/search")
    public List<User> searchUsers(@RequestParam(required = false) String username) {
        List<User> list = userMapper.selectUserByUsername(username);
        return list;
    }

    //    新增
    @PostMapping("/save")
    public Map<String, Object> save(@RequestBody User user) {
        Map<String, Object> result = new HashMap<>();
        int insertResult = userMapper.insertUser(user);
        if (insertResult > 0) {
            result.put("code", 200);
            result.put("msg", "新增成功,新id=" + user.getId());
        } else {
            result.put("code", 500);
            result.put("msg", "新增失败");
        }
        return result;
    }

    //    修改
    @PutMapping("/update")
    public Map<String, Object> update(@RequestBody User user) {
        Map<String, Object> result = new HashMap<>();
        int updateResult = userMapper.updateUser(user);
        if (updateResult > 0) {
            result.put("code", 200);
            result.put("msg", "修改成功");
        } else {
            result.put("code", 500);
            result.put("msg", "未找到id=" + user.getId() + "的用户");
        }
        return result;
    }

    //    删除
    @DeleteMapping("/delete/{id}")
    public Map<String, Object> delete(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        int deleteResult = userMapper.deleteUser(id);
        if (deleteResult > 0) {
            result.put("code", 200);
            result.put("msg", "删除成功");
        } else {
            result.put("code", 500);
            result.put("msg", "未找到id=" + id + "的用户");
        }
        return result;
    }

}
