package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import entity.User;
import service.MusicService;
import service.ReLogin;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet ("/loveMusicServlet")
public class LoveMusicServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        //首先判断是否登录
        User user = (User)req.getSession().getAttribute("user");
        Map<String , Object> map = new HashMap<>();
        if(user == null) {
            //如果没登陆就给出提示
            map.put("msg","login");
        } else {
            resp.setContentType("application/json;charset=utf-8");
            //如果已经登陆则获取要喜欢的id
            String idStr = req.getParameter("id");
            int id = Integer.parseInt(idStr);
            //判断是否已经在喜欢列表中了,在这之前还要拿到用户信息;
            int userid = user.getId();
            MusicService musicService = new MusicService();
            //使用like来接受判断结果
            boolean like = musicService.findMusicByMusicId(userid, id);
            //如果已经是喜欢的则添加失败,否则成功
            if(like) {
                System.out.println("已在喜欢列表");
                map.put("msg", false);
            } else {
                //开始添加
                boolean res = musicService.insertLoveMusic(userid, id);
                if(res) {
                    System.out.println("添加喜欢成功");
                    map.put("msg", true);
                } else {
                    System.out.println("添加喜欢失败!");
                    map.put("msg", false);
                }
            }
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(resp.getWriter(), map);
    }
}
