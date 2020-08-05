package service;

import dao.UserDao;
import entity.User;

public class UserService {
    UserDao userDao = new UserDao();

    public User login(User loginUser){
        User user = userDao.login(loginUser);
        return user;
    }
    public int ifUser(String username) {
        int ret = userDao.ifUser(username);
        return ret;
    }
    public void insertUser(User user) {
        userDao.inserUser(user);
    }
}
