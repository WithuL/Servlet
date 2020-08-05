package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import entity.User;
import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/registServlet")
public class RegistServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");
        Map<String, Object> map = new HashMap<>();
        //获取对应的用户名密码
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String email = req.getParameter("email");
        //查看是否已存在该用户
        UserService   userService = new UserService();
        int ret = userService.ifUser(username);
        if(ret != 0) {
            System.out.println("注册失败");
            map.put("msg", false);
        } else {
            User user = new User();
            user.setUserName(username);
            user.setPassword(password);
            user.setGender("未知");
            user.setAge(20);
            user.setEmail(email);
            userService.insertUser(user);
            map.put("msg", true);
            System.out.println("注册成功");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(resp.getWriter(), map);
        System.out.println("返沪成功");
    }
}
