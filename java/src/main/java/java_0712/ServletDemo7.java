package java_0712;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ServletDemo7 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Cookie userName = new Cookie("UserName","lcp");
        Cookie age = new Cookie("Age","18");
        resp.addCookie(userName);
        resp.addCookie(age);
        resp.setContentType("text/html; charset=utf-8");
        resp.getWriter().write("返回cookie成功");
    }
}
