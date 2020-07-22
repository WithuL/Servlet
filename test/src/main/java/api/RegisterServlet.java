package api;

import model.User;
import model.UserDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RegisterServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1.获取到前端提交的数据,检验是否合法
        resp.setContentType("text/html; charset=utf-8");
        String name = req.getParameter("name");
        String password = req.getParameter("password");
        if((name == null || "".equals(name)) ||(password == null || "".equals(password))) {
            String html = HtmlGenerator.getMessagePage("用户名或密码无效","register.html");
            resp.getWriter().write(html);
            return;
        }
        UserDao userDao = new UserDao();
        //2.看看当前的用户名在数据库中是否已存在,如果存在则注册失败
        User existUser = userDao.selectByName(name);
        if(existUser != null) {
            String html = HtmlGenerator.getMessagePage("用户名已存在!", "register.html");
            resp.getWriter().write(html);
            return;
        }

        //3.如果不存在则生成一个User对象,插入到数据库中
        //4.并返回注册成功
        User user = new User();
        user.setName(name);
        user.setPassWord(password);
        userDao.add(user);
        String html = HtmlGenerator.getMessagePage("注册成功!,点击跳转 ","login.html");
        resp.getWriter().write(html);
    }
}
