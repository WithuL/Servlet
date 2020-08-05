package servlet;

import entity.User;
import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/manageIfLogin")
public class ManageIfLogin extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        UserService userService = new UserService();

        User user = (User)req.getSession().getAttribute("user");
        if(user == null) {
            resp.sendRedirect("/onlineMusic/login.html");
        }else {
            resp.sendRedirect("/onlineMusic/manage.html");
        }
    }
}
