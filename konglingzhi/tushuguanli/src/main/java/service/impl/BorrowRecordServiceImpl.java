package service.impl;


import dao.BorrowRecordDao;
import dao.impl.BorrowRecordDaoImpl;
import entity.BorrowRecord;
import service.BorrowRecordService;


import java.util.List;


public class BorrowRecordServiceImpl implements BorrowRecordService {


    private final BorrowRecordDao borrowRecordDao = new BorrowRecordDaoImpl();


    @Override
    public List<BorrowRecord> findAll(){
        return borrowRecordDao.findAll();
    }
}