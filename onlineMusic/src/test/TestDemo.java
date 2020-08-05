package test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.MusicDao;
import entity.Music;
import entity.User;
import service.UserService;

import java.util.ArrayList;
import java.util.Date;

public class TestDemo {
    public static void main(String[] args) throws JsonProcessingException {
        UserService userService = new UserService();
        User user = new User();
        user.setUserName("lcp");
        user.setPassword("123");
        user = userService.login(user);
        System.out.println(user.toString());
    }
}
