package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import entity.Student;
import util.JDBCUtil;

public class StudentDao {
    private Connection conn;
    private PreparedStatement pstmt;
    private ResultSet rs;

    public List<Student> findAll() {
        List<Student> list=new ArrayList<>();
        String sql="select * from student";
        try {
            conn=JDBCUtil.getConnection();
            pstmt=conn.prepareStatement(sql);
            rs =pstmt.executeQuery();
            while (rs.next()){
                Student stu=new Student();
                stu.setId(rs.getInt("id"));
                stu.setName(rs.getString("name"));
                stu.setAge(rs.getInt("age"));
                list.add(stu);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            JDBCUtil.close(conn,pstmt,rs);
        }
        return list;
    }

    public int insert(Student stu) {
        String sql = "INSERT INTO student (name, age) VALUES (?, ?)";
        try {
            conn = JDBCUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, stu.getName());
            pstmt.setInt(2, stu.getAge());
            return pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(conn, pstmt);
        }
        return 0;
    }

    public int update(Student stu) {
        String sql = "UPDATE student SET name=?, age=? WHERE id=?";
        try {
            conn = JDBCUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, stu.getName());
            pstmt.setInt(2, stu.getAge());
            pstmt.setInt(3, stu.getId());
            return pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(conn, pstmt);
        }
        return 0;
    }

    public int deleteById(Integer id) {
        String sql = "DELETE FROM student WHERE id=?";
        try {
            conn = JDBCUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            return pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(conn, pstmt);
        }
        return 0;
    }

}
