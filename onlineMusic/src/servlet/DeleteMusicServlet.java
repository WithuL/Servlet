package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import entity.Music;
import service.MusicService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet ("/deleteServlet")
public class DeleteMusicServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");
        //先获取音乐id
        int id = Integer.parseInt(req.getParameter("id"));
        //然后开始删除
        MusicService musicService = new MusicService();
        //判断数据库中是否有这首歌
        Music music = musicService.findMusicById(id);
        if(music == null) {
            return;
        }
        //如果有就开始删除数据库数据
        int ret = musicService.deleteMusicById(id);
        System.out.println(id);
        //如果数据库数据删除成功,则删除硬盘上的音乐数据
        Map<String, Object>  map = new HashMap<>();
        if(ret == 1) {
            File file = new File("/root/apache-tomcat-8.5.57/webapps/onlineMusic" + music.getUrl() + ".mp3");
            if (file.delete()) {
                System.out.println("硬盘数据删除成功");
                map.put("msg", true);
            }else {
                System.out.println("硬盘数据删除失败");
                map.put("msg", false);
            }
        } else {
            //如果数据库删除失败
            map.put("msg", false);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writeValueAsString(music));
        objectMapper.writeValue(resp.getWriter(), map);
    }
}
