package service.impl;


import dao.UserDao;
import dao.impl.UserDaoImpl;
import entity.User;
import service.UserService;


import java.util.List;


public class UserServiceImpl implements UserService {


    private final UserDao userDao = new UserDaoImpl();


    @Override
    public boolean addUser(User user) {
        return userDao.addUser(user);
    }


    @Override
    public User findById(int id) {
        return userDao.findById(id);
    }


    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }


    @Override
    public User login(String username, String password) {
        return userDao.login(username, password);
    }


}