package dao.impl;


import dao.BookDao;
import entity.Book;
import util.JDBCUtil;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class BookDaoImpl implements BookDao {
    @Override
    public boolean addBook(Book book){
        String sql = "insert into book(book_name,author," +
                "price,stock,cid) values(?,?,?,?,?)";
        Connection conn = JDBCUtil.getConn();
        PreparedStatement pstmt = null;
        try{
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,book.getBookName());
            pstmt.setString(2,book.getAuthor());
            pstmt.setBigDecimal(3,book.getPrice());
            pstmt.setInt(4,book.getStock());
            pstmt.setInt(5,book.getCid());
            return pstmt.executeUpdate() > 0;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }finally {
            JDBCUtil.close(conn,pstmt);
        }
    }




    @Override
    public boolean updateStock(int id, int num){
        String sql = "update book set stock = stock + ? where id = ?";
        Connection conn = JDBCUtil.getConn();
        PreparedStatement pstmt = null;
        try{
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,num);
            pstmt.setInt(2,id);
            return pstmt.executeUpdate() > 0;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }finally {
            JDBCUtil.close(conn,pstmt);
        }
    }




    public List<Book> findAll(){
        List<Book> list = new ArrayList<>();
        String sql = "select * from book";
        Connection conn = JDBCUtil.getConn();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()){
                Book b = new Book();
                b.setId(rs.getInt("id"));
                b.setBookName(rs.getString("book_name"));
                b.setAuthor(rs.getString("author"));
                b.setPrice(rs.getBigDecimal("price"));
                b.setStock(rs.getInt("stock"));
                list.add(b);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            JDBCUtil.close(conn,pstmt,rs);
        }
        return list;
    }




    public Book findById(int id){
        String sql = "select * from book where id = ?";
        Connection conn = JDBCUtil.getConn();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,id);
            rs = pstmt.executeQuery();
            if(rs.next()){
                Book b = new Book();
                b.setId(rs.getInt("id"));
                b.setBookName(rs.getString("book_name"));
                b.setStock(rs.getInt("stock"));
                return b;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            JDBCUtil.close(conn,pstmt,rs);
        }
        return null;
    }




    public List<Book> findByLikeName(String name){
        List<Book> list = new ArrayList<>();
        String sql = "select * from book where book_name like ?";
        Connection conn = JDBCUtil.getConn();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,"%" + name + "%");
            rs = pstmt.executeQuery();
            while (rs.next()){
                Book b = new Book();
                b.setId(rs.getInt("id"));
                b.setBookName(rs.getString("book_name"));
                b.setAuthor(rs.getString("author"));
                b.setPrice(rs.getBigDecimal("price"));
                b.setStock(rs.getInt("stock"));
                list.add(b);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            JDBCUtil.close(conn,pstmt,rs);
        }
        return list;
    }
}