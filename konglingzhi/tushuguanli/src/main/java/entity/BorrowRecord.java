package entity;


import java.util.Date;


public class BorrowRecord {
    private Integer id;
    private Integer bookId;
    private Integer userId;
    private Date borrowTime;
    private Date returnTime;
    private Integer status;


    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
        this.id = id;
    }


    public Integer getBookId() {
        return bookId;
    }


    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }


    public Integer getUserId() {
        return userId;
    }


    public void setUserId(Integer userId) {
        this.userId = userId;
    }


    public Date getBorrowTime() {
        return borrowTime;
    }


    public void setBorrowTime(Date borrowTime) {
        this.borrowTime = borrowTime;
    }


    public Date getReturnTime() {
        return returnTime;
    }


    public void setReturnTime(Date returnTime) {
        this.returnTime = returnTime;
    }


    public Integer getStatus() {
        return status;
    }


    public void setStatus(Integer status) {
        this.status = status;
    }


    @Override
    public String toString() {
        return "记录编号：" +id +
                ", 用户id：" + userId +
                ", 图书id：" + bookId +
                ", 借阅时间：" + borrowTime +
                ", 状态：" + (status == 1 ? "未还" : "已还");
    }
}