package util;

import java.sql.*;

public class JDBCUtil {
    private static final String DRIVER="com.mysql.cj.jdbc.Driver";
    private static final String URL="jdbc:mysql://localhost:3306/sch";
    private static final String USER="root";
    private static final String PASSWORD="root";

    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    //关闭连接
    public static void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs!=null) rs.close();
            if (pstmt!=null) pstmt.close();
            if (conn!=null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //重写方法
    public static void close(Connection conn, PreparedStatement pstmt){
        close(conn,pstmt,null);
    }

}
