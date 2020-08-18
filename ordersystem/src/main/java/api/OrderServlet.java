package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.org.apache.xpath.internal.operations.Or;
import javafx.util.Builder;
import model.*;
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

public class OrderServlet extends HttpServlet {
    static class Response{
        String reason;
        int ok;
    }

    private Gson gson = new GsonBuilder().create();
    //8.新增订单
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Response response = new Response();
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=utf-8");
        try{
            HttpSession session = req.getSession(false);
            if(session == null) {
                throw new OrderSystemException("您尚未登陆");
            }
            User user = (User) session.getAttribute("user");
            if(user == null) {
                throw new OrderSystemException("您尚未登陆");
            }
            if(user.getIsAdmin() == 1) {
                throw new OrderSystemException("管理员无法下单");
            }
            String body = ReqReader.readBody(req);
            Integer[] dishids = gson.fromJson(body, Integer[].class);
            Order order = new Order();
            order.setUserid(user.getId());
            List<Dish> dishes = new ArrayList<>();
            for(Integer x: dishids) {
                Dish dish = new Dish();
                dish.setDishid(x);
                dishes.add(dish);
            }
            order.setDishes(dishes);
            OrderDao orderDao = new OrderDao();
            orderDao.add(order);
            response.ok = 1;
            response.reason = "";

        }catch (OrderSystemException e) {
            response.ok = 0;
            response.reason = e.getMessage();
            e.printStackTrace();
        }finally {
            resp.setContentType("application/json; charset=utf-8");
            String jsonString = gson.toJson(response);
            resp.getWriter().write(jsonString);
        }
    }

    //9.查看所有订单
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=utf-8");
        Response response = new Response();
        List<Order> orders = new ArrayList<>();

        try{
            HttpSession session = req.getSession();
            if(session == null) {
                throw new OrderSystemException("您尚未登陆");
            }
            User user = (User) session.getAttribute("user");
            if(user == null) {
                throw new OrderSystemException("您尚未登陆");
            }
            OrderDao orderDao = new OrderDao();
            String orderIdStr = req.getParameter("orderId");
            //如果没有传入订单编号就查找该用户所有订单
            if(orderIdStr == null) {
                if(user.getIsAdmin() == 0) {
                    //如果是普通用户查找个人订单
                    orders = orderDao.selectByUserId(user.getId());
                }else{  //如果用户是管理员则查找所有订单
                    orders = orderDao.selectAll();
                }
                String jsonString = gson.toJson(orders);
                resp.getWriter().write(jsonString);
            }else {
                //如果有订单编号,那么就查找订单
                //10.查看订单详情
                int orderid = Integer.parseInt(orderIdStr);
                Order order = orderDao.selectById(orderid);
                //如果当前订单userid和当前登录用户userid不相符
                if(user.getIsAdmin() == 0 && user.getId() != order.getUserid() ) {
                    throw new OrderSystemException("对不起,您无权查看他人订单");
                }
                //4.构造响应结果
                String jsonString = gson.toJson(order);
                resp.getWriter().write(jsonString);
            }
        }catch (OrderSystemException e) {
            String jsonString = gson.toJson(orders);
            resp.getWriter().write(jsonString);
        }
    }

    //11.修改订单状态

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
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
            //读取请求中的orderid和isdone
            String orderIdStr = req.getParameter("orderId");
            String isDoneStr = req.getParameter("isDone");
            if(orderIdStr ==  null || isDoneStr == null ) {
                throw new OrderSystemException("参数有误");
            }
            //开始操作
            OrderDao orderDao = new OrderDao();
            int orderid = Integer.parseInt(orderIdStr);
            int isDone = Integer.parseInt(isDoneStr);
            orderDao.changeState(orderid, isDone);
            response.ok = 1;
            response.reason = "";
        } catch (OrderSystemException e) {
            response.ok = 0;
            response.reason = e.getMessage();
            e.printStackTrace();
        } finally {
            resp.setContentType("application/json; charset=utf-8");
            String jsonString = gson.toJson(response);
            resp.getWriter().write(jsonString);
        }
    }
}
