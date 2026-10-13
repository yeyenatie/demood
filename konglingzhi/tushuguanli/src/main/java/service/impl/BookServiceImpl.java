package service.impl;


import dao.BookDao;
import dao.BorrowRecordDao;
import dao.impl.BookDaoImpl;
import dao.impl.BorrowRecordDaoImpl;
import entity.Book;
import entity.BorrowRecord;
import service.BookService;
import util.JDBCUtil;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public class BookServiceImpl implements BookService {


    private final BookDao bookDao = new BookDaoImpl();
    private final BorrowRecordDao borrowRecordDao = new BorrowRecordDaoImpl();
    @Override
    public boolean addBook(Book book) {
        return bookDao.addBook(book);
    }


    @Override
    public List<Book> findAllBooks() {
        return bookDao.findAll();
    }


    @Override
    public List<Book> findBookLikeName(String name) {
        return bookDao.findByLikeName(name);
    }


    @Override
    public boolean borrowBook(int userId, int bookId) {
        Connection conn = JDBCUtil.getConn();
        try {
            conn.setAutoCommit(false);
            Book  book = bookDao.findById(bookId);
            if(book == null || book.getStock() <= 0){
                conn.rollback();
                return false;
            }
            bookDao.updateStock(bookId, -1);
            BorrowRecord record = new BorrowRecord();
            record.setUserId(userId);
            record.setBookId(bookId);
            borrowRecordDao.addRecord(record);
            conn.commit();
            return true;
        }catch (Exception e){
            try{
                conn.rollback();
            }catch (SQLException ex){
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        }finally {
            try {
                conn.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }




    @Override
    public boolean returnBook(int recordId) {
        Connection conn = JDBCUtil.getConn();
        try {
            conn.setAutoCommit(false);
            String sql = "select book_id from borrow_record where id = ? " +
                    "and status = 1";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,recordId);
            ResultSet rs = pstmt.executeQuery();
            if(!rs.next()){
                conn.rollback();
                return false;
            }
            int bookId = rs.getInt("book_id");
            bookDao.updateStock(bookId, 1);
            borrowRecordDao.updateReturn(recordId);
            conn.commit();
            return true;
        }catch (Exception e){
            try{
                conn.rollback();
            }catch (SQLException ex){
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        }finally {
            try {
                conn.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }


}