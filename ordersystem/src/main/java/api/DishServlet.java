package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.DBUtil;
import model.Dish;
import model.DishDao;
import model.User;
import util.OrderSystemException;
import util.ReqReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class DishServlet extends HttpServlet {
    private Gson gson = new GsonBuilder().create();

    static class Request {
        String name;
        int price;
    }

    static class Response {
        int ok;
        String reason;
    }
    @Override
    //5.新增菜品
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String body = ReqReader.readBody(req);
        Request request = gson.fromJson(body, Request.class);
        Response response = new Response();
        try {
            HttpSession session = req.getSession(false);
            if(session == null) {
                throw new OrderSystemException("请登陆后操作");
            }
            User user = (User)session.getAttribute("user");
            if(user == null) {
                throw new OrderSystemException("请登陆后操作");
            }
            if(user.getIsAdmin() == 0 ) {
                throw new OrderSystemException("仅管理员可进行操作");
            }
            Dish dish = new Dish();
            dish.setName(request.name);
            dish.setPrice(request.price);
            DishDao dishDao = new DishDao();
            dishDao.add(dish);
            response.ok = 1;
            response.reason = "";
        }catch (OrderSystemException e) {
            response.ok = 0;
            response.reason = e.getMessage();
            e.printStackTrace();
        }finally {
            resp.setContentType("application/json; charset=utf-8");
            String json = gson.toJson(response);
            resp.getWriter().write(json);
        }
    }

    //6.删除菜品
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Response response = new Response();
        try {
            HttpSession session = req.getSession(false);
            if (session == null) {
                throw new OrderSystemException("请登陆后操作");
            }
            User user = (User) session.getAttribute("user");
            if (user == null) {
                throw new OrderSystemException("请登陆后操作");
            }
            if (user.getIsAdmin() == 0) {
                throw new OrderSystemException("仅管理员可进行操作");
            }
            String dishIdStr = req.getParameter("dishId");
            if (dishIdStr == null) {
                throw new OrderSystemException("dishid参数不正确");
            }
            int dishid = Integer.parseInt(dishIdStr);
            DishDao dishDao = new DishDao();
            dishDao.delete(dishid);
            response.ok = 1;
            response.reason = "";
        } catch (OrderSystemException e) {
            response.ok = 0;
            response.reason = e.getMessage();
            e.printStackTrace();
        } finally {
            resp.setContentType("application/json; charset=utf-8");
            String json = gson.toJson(response);
            resp.getWriter().write(json);
        }
    }
    //7.查看所有菜品

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=utf-8");
        List<Dish> dishes = new ArrayList<>();
        Response response = new Response();
        req.setCharacterEncoding("utf-8");
        try{
            HttpSession session = req.getSession();
            if(session == null) {
                throw new OrderSystemException("您尚未登录,请登陆后操作");
            }
            User user = (User) session.getAttribute("user");
            if(user == null) {
                throw new OrderSystemException("您尚未登录,请登陆后操作");
            }
            DishDao dishDao = new DishDao();
            dishes = dishDao.selectAll();
            String jsonString = gson.toJson(dishes);
            resp.getWriter().write(jsonString);
        }catch (OrderSystemException e) {
            String jsonString = gson.toJson(dishes);
            resp.getWriter().write(jsonString);
        }
    }
}
