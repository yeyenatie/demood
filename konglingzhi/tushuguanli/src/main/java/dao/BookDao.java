package dao;


import entity.Book;


import java.util.List;


public interface BookDao {


    boolean addBook(Book book);


    boolean updateStock(int id, int num);


    List<Book> findAll();


    Book findById(int id);


    List<Book> findByLikeName(String name);


}