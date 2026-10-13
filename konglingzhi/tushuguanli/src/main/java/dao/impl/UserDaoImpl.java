package dao.impl;


import dao.UserDao;
import entity.User;
import util.JDBCUtil;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class UserDaoImpl implements UserDao {


    @Override
    public boolean addUser(User user) {
        String sql = "insert into user(username,password,real_name,phone) values(?,?,?,?)";
        Connection conn = JDBCUtil.getConn();
        PreparedStatement pstmt = null;
        try{
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,user.getUsername());
            pstmt.setString(2,user.getPassword());
            pstmt.setString(3,user.getRealName());
            pstmt.setString(4,user.getPhone());
            return pstmt.executeUpdate() > 0;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }finally {
            JDBCUtil.close(conn,pstmt);
        }
    }


    @Override
    public User findById(int id) {
        String sql = "select * from user where id = ?";
        Connection conn = JDBCUtil.getConn();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,id);
            rs = pstmt.executeQuery();
            if (rs.next()){
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRealName(rs.getString("real_name"));
                user.setPhone(rs.getString("phone"));
                return user;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            JDBCUtil.close(conn,pstmt,rs);
        }
        return null;
    }


    @Override
    public List<User> findAll() {
        List<User> list = new ArrayList<>();
        String sql = "select * from user";
        Connection conn = JDBCUtil.getConn();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()){
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setRealName(rs.getString("real_name"));
                user.setPhone(rs.getString("phone"));
                list.add(user);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            JDBCUtil.close(conn,pstmt,rs);
        }
        return list;
    }


    @Override
    public User login(String username, String password) {
        String sql = "select * from user where username = ? and password = ?";
        Connection conn = JDBCUtil.getConn();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,username);
            pstmt.setString(2,password);
            rs = pstmt.executeQuery();
            if (rs.next()){
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setRealName(rs.getString("real_name"));
                return user;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            JDBCUtil.close(conn,pstmt,rs);
        }
        return null;
    }


}