package api;


import model.User;
import model.UserDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1.获取到用户名和密码并校验
        resp.setContentType("text/html; charset=utf-8");
        String name = req.getParameter("name");
        String password = req.getParameter("password");
        if((name == null || "".equals(name) || (password == null || "".equals(password)))) {
            String html = HtmlGenerator.getMessagePage("登陆名或密码错误!", "login.html");
            resp.getWriter().write(html);
            return;
        }
        UserDao userDao = new UserDao();
        User user = userDao.selectByName(name);
        //2.查找是否有该用户
        //3.对比密码
        if(user == null || !password.equals(user.getPassWord())) {
            String html = HtmlGenerator.getMessagePage("用户名或密码错误!","login.html");
            resp.getWriter().write(html);
            return;
        }
        //4.密码一直则登陆成功
        HttpSession httpSession = req.getSession(true);
        httpSession.setAttribute("user",user);
        //5.返回
        String html = HtmlGenerator.getMessagePage("登陆成功~","article");
        resp.getWriter().write(html);
    }
}
