package util;


import java.sql.*;




public class JDBCUtil {


    private static final String URL = "jdbc:mysql://localhost:3306/tushuguanlixitong?" +
            "useUnicode=true&characterEncoding=utf8";
    private static final String USER = "root";
    private static final String PASSWORD = "root";


    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public static Connection getConn() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        }catch (SQLException e){
            return null;
        }
    }


    public static void close(Connection conn, PreparedStatement pstmt) {
        try{
            if (pstmt != null){
                pstmt.close();
            }
            if (conn != null){
                conn.close();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }


    public static void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try{
            if (rs != null){
                rs.close();
            }
            if (pstmt != null){
                pstmt.close();
            }
            if (conn != null){
                conn.close();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

}