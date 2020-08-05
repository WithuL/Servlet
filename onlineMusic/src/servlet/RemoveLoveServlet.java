package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import entity.User;
import service.MusicService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet ("/removeLoveServlet")
public class RemoveLoveServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        Map<String, Object> map = new HashMap<>();

        //先获取用户id和音乐id
        User user = (User)req.getSession().getAttribute("user");
        int userid = user.getId();
        int musicid = Integer.parseInt(req.getParameter("id"));
        //尝试去删除喜欢的列表
        MusicService musicService = new MusicService();
        int ret = musicService.removeLoveMusic(userid, musicid);
        //如果删除成功
        if(ret == 1) {
            map.put("msg", true);
        } else {
            map.put("msg", false);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(resp.getWriter(), map);
    }
}
