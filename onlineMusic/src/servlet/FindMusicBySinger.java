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
import java.util.List;

@WebServlet("/findMusicBySinger")
public class FindMusicBySinger extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");

        MusicService musicService = new MusicService();
        List<Music> musicslist = null;

        String musicName = req.getParameter("musicName");

        System.out.println("musicName:"+musicName);
        System.out.println("进入查询");
        //进行查询
        System.out.println("有关键字");

        musicslist = musicService.findMusicBySingerName(musicName);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(resp.getWriter(), musicslist);

    }
}
