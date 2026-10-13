package service;


import entity.BorrowRecord;


import java.util.List;


public interface BorrowRecordService {
    List<BorrowRecord> findAll();
}