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
import java.io.IOException;
@WebServlet("/register")
public class RegisterServelt extends HttpServlet {
    private Gson gson = new GsonBuilder().create();
    //读取的JSON请求对象
    static class Request {
        public String name;
        public String password;
    }
    //发送的JSON响应对象
    static class Responce {
        int ok; //0代表失败, 1成功
        String reason ;
    }

    //1.注册
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Responce responce = new Responce();
        try {
            //1.先读取body中的数据,
            String body = ReqReader.readBody(req);
            //2.把body中的数据解析成Requsest对象
            Request request = gson.fromJson(body, Request.class);
            //3.查询数据库,看是否重复
            UserDao userDao = new UserDao();
            User existUser = userDao.selectByName(request.name);
            if(existUser != null) {
                throw new OrderSystemException("当前用户名已经存在");
            }
            //4.如果不重复就把用户名密码构造成User对象插入
            User user= new User();
            user.setName(request.name);
            user.setPassword(request.password);
            user.setIsAdmin(0);
            userDao.add(user);
            responce.ok = 1;
            responce.reason = "";

        } catch (OrderSystemException e) {
            e.printStackTrace();
            responce.ok = 0;
            responce.reason = e.getMessage();
        }
        //5.构造响应
        finally {
            String jsonString = gson.toJson(responce);
            resp.setContentType("application/json; charset=utf-8");
            resp.getWriter().write(jsonString);
        }
    }
}
