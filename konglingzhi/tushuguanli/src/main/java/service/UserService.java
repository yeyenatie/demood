package service;


import entity.User;


import java.util.List;


public interface UserService {


    boolean addUser(User user);


    User findById(int id);


    List<User> findAll();


    User login(String username, String password);
}