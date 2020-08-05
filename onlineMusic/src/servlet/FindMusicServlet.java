package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import entity.Music;
import service.MusicService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/findMusic")
public class FindMusicServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");

        //根据前端传过来的歌曲名进行查找
        String musicName = req.getParameter("musicName");

        MusicService musicService = new MusicService();
        List<Music> musicslist = null;

        //进行查询
        if(musicName == null) {
            //如果没有关键字则查找所有歌曲
            musicslist = musicService.findMusic();
        }else {
            //如果有关键字就查找相应歌曲
            musicslist = musicService.ifMusic(musicName);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(resp.getWriter(), musicslist);

    }
}
