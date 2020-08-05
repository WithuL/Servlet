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

@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");

        //读取请求体内容
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        //构造登陆对象
        User loginUser = new User();
        loginUser.setUserName(username);
        loginUser.setPassword(password);

        //尝试登录
        UserService userService = new UserService();
        loginUser = userService.login(loginUser);

        //构造返回内容
        Map<String, Object> return_map = new HashMap<>();

        //按照登录是否成功进行逻辑处理
        if(loginUser == null) {
            System.out.println("登录失败!");
            return_map.put("msg",false);
        } else {
            System.out.println("登陆成功!");

            //将该用户的信息写入到session中持续访问
            req.getSession().setAttribute("user", loginUser);
            return_map.put("msg", true);
        }

        //将retur_map返回给前端
        //构造成json对象
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(resp.getWriter(), return_map);
    }
}
