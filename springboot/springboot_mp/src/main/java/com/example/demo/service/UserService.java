package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.dto.UserDTO;
import com.example.demo.entity.User;
import com.example.demo.vo.UserVO;

import java.util.List;

//用户服务接口，IService封装了基础的CRUD
public interface UserService extends IService<User> {

    //    新增或修改用户
//    @param userDTO 用户入参
//    @return保存成功
    boolean saveUser(UserDTO userDTO);

    //    查询用户列表
//    @return 用户V0列表
    List<UserVO> getUserList();

    //用户注册
    UserVO register(UserDTO userDTO);

    //用户登录
    UserVO login(UserDTO userDTO);


}
