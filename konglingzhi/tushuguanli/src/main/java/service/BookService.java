package service;


import entity.Book;


import java.util.List;


public interface BookService {


    boolean addBook(Book book);


    List<Book> findAllBooks();


    List<Book> findBookLikeName(String name);


    boolean borrowBook(int userId,int bookId);


    boolean returnBook(int recordId);


}