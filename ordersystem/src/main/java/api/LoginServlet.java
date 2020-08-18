package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.User;
import model.UserDao;
import util.OrderSystemException;
import util.ReqReader;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    //读取的JSON请求对象
    static class Request {
        public String name;
        public String password;
    }
    //发送的JSON响应对象
    static class Responce {
        int ok; //0代表失败, 1成功
        String reason ;
        String name;
        int isAdmin;

    }
    private Gson gson = new GsonBuilder().create();

    //2.登录
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        Responce responce = new Responce();
        //1.先读取req
        try {
            String body = ReqReader.readBody(req);
            Request request = gson.fromJson(body, Request.class);
            UserDao userDao = new UserDao();
            User user = userDao.selectByName(request.name);
            if(user == null || !user.getPassword().equals(request.password)) {
                throw new OrderSystemException("您输入的用户名有误,请重试!");
            }
            HttpSession session = req.getSession(true);
            session.setAttribute("user", user);
            responce.ok = 1;
            responce.isAdmin = user.getIsAdmin();
            responce.name = user.getName();
            responce.reason = "";
        } catch (OrderSystemException e) {
            responce.ok = 0;
            responce.reason = e.getMessage();
            e.printStackTrace();
        } finally {
            resp.setContentType("application/json; charset=utf-8");
            String jsonString = gson.toJson(responce);
            resp.getWriter().write(jsonString);
        }
    }

    //3.检测登陆状态
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf-8");
        Responce responce = new Responce();
        //检测是否登录,session不存在则未登录
            try {
                HttpSession session = req.getSession(false);
                if(session == null) {
                    throw new OrderSystemException("您尚未登陆");
                }
                User user = (User) session.getAttribute("user");
                if(user == null) {
                    throw new OrderSystemException("您尚未登陆");
                }
                //到此处则已登录
                responce.name = user.getName();
                responce.reason = "";
                responce.isAdmin = user.getIsAdmin();
                responce.ok = 1;
            } catch (OrderSystemException e) {
                responce.ok = 0;
                responce.reason = e.getMessage();
                e.printStackTrace();
            }finally {
                resp.setContentType("application/json; utf-8");
                String jsonString = gson.toJson(responce);
                resp.getWriter().write(jsonString);
            }
    }
}
