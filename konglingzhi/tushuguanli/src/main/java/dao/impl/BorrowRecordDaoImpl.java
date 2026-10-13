package dao.impl;


import dao.BorrowRecordDao;
import entity.BorrowRecord;
import util.JDBCUtil;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class BorrowRecordDaoImpl implements BorrowRecordDao {
    @Override
    public boolean addRecord(BorrowRecord borrowRecord) {
        String sql = "insert into borrow_record(user_id,book_id,borrow_time," +
                "status) values(?,?,now(),1)";
        Connection conn = JDBCUtil.getConn();
        PreparedStatement pstmt = null;
        try{
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,borrowRecord.getUserId());
            pstmt.setInt(2,borrowRecord.getBookId());
            return pstmt.executeUpdate() > 0;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }finally {
            JDBCUtil.close(conn,pstmt);
        }
    }


    @Override
    public boolean updateReturn(int recordId) {
        String sql = "update borrow_record set status = 2," +
                "return_time = now() where id = ?";
        Connection conn = JDBCUtil.getConn();
        PreparedStatement pstmt = null;
        try{
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,recordId);
            return pstmt.executeUpdate() > 0;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }finally {
            JDBCUtil.close(conn,pstmt);
        }
    }


    @Override
    public List<BorrowRecord> findAll() {
        List<BorrowRecord> list = new ArrayList<>();
        String sql = "select * from borrow_record";
        Connection conn = JDBCUtil.getConn();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()){
                BorrowRecord record = new BorrowRecord();
                record.setId(rs.getInt("id"));
                record.setUserId(rs.getInt("user_id"));
                record.setBookId(rs.getInt("book_id"));
                record.setBorrowTime(rs.getDate("borrow_time"));
                record.setStatus(rs.getInt("status"));
                list.add(record);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            JDBCUtil.close(conn,pstmt,rs);
        }
        return list;
    }


}