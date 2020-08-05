package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import entity.Music;
import entity.User;
import service.MusicService;
import service.ReLogin;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.html.HTML;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/findLoveMusic")
public class FIndLoveMusicServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");

        User user = (User)req.getSession().getAttribute("user");
        if(user == null) {
            //如果没登陆就给出提示
            resp.setStatus(302);
            resp.addHeader("location","/onlineMusic/login.html");
//            resp.setContentType("text/html;charset=utf-8");
            System.out.println("用户未登录");
//
//            String res = ReLogin.relogin();
//            resp.getWriter().write(res);

        } else {
            //如果登陆了就先获取查询的音乐名
            resp.setContentType("application/json;charset=utf-8");

            String loveMusicName = req.getParameter("loveMusicName");
            MusicService musicService = new MusicService();
            int userid = user.getId();
            //如果有音乐名就只模糊查找该音乐
            List<Music> musics = null;
            if (loveMusicName == null) {
                musics = musicService.findLoveMusic(userid);
            } else {
                //如果有音乐名则查找音乐
                musics = musicService.ifMusicLove(loveMusicName, userid);
                System.out.println(musics);
            }
            //开始返回
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(resp.getWriter(), musics);
        }
    }
}
