package dao;


import entity.BorrowRecord;


import java.util.List;


public interface BorrowRecordDao {


    boolean addRecord(BorrowRecord borrowRecord);


    boolean updateReturn(int recordId);


    List<BorrowRecord> findAll();
}