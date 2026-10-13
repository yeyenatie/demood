package entity;


import java.math.BigDecimal;


public class Book {
    private Integer id;
    private String bookName;
    private String author;
    private BigDecimal price;
    private Integer stock;
    private Integer cid;


    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
        this.id = id;
    }


    public String getBookName() {
        return bookName;
    }


    public void setBookName(String bookName) {
        this.bookName = bookName;
    }


    public BigDecimal getPrice() {
        return price;
    }


    public void setPrice(BigDecimal price) {
        this.price = price;
    }


    public String getAuthor() {
        return author;
    }


    public void setAuthor(String author) {
        this.author = author;
    }


    public Integer getStock() {
        return stock;
    }


    public void setStock(Integer stock) {
        this.stock = stock;
    }


    public Integer getCid() {
        return cid;
    }


    public void setCid(Integer cid) {
        this.cid = cid;
    }


    @Override
    public String toString() {
        return "图书编号：" + id +
                ", 书名：" + bookName +
                ", 作者：" + author +
                ", 价格：" + price +
                ", 库存：" + stock;
    }
}