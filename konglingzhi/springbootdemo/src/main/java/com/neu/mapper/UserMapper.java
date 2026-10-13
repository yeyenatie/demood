package com.neu.mapper;

import com.neu.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

//mapper接口
public interface UserMapper {

    //    查询全部用户
    List<User> selectAll();

    //    按id查询单个
    User selectById(Integer id);

    //    按用户名模糊查询
    List<User> selectUserByUsername(@Param("username") String username);

    //    新增
    int insertUser(User user);

    //    修改
    int updateUser(User user);

    //    删除
    int deleteUser(Integer id);

}
