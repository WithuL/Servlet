package servlet;

import entity.User;
import service.MusicService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;

@WebServlet ("/uploadsucess")
public class UploadSucessServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        //获取歌手信息
        String singer = req.getParameter("singer");
        //获取文件信息
        String fileMessage = (String)req.getSession().getAttribute("fileName");
        //获取文件名
        String  fileName = fileMessage.split("\\.")[0];
        //获取当前时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String time = sdf.format(new Date());
        //获取userId
        User user = (User)req.getSession().getAttribute("user");
        int userid = user.getId();
        //获取文件存放路径
        String url = "music/" + fileName ;
        //上传信息到数据库;
        MusicService musicService = new MusicService();
        int ret = musicService.insert(fileName, singer, time, url , userid);
        if(ret == 1) {
            System.out.println("上传至数据库成功!");
            resp.sendRedirect("/onlineMusic/manage.html");
        } else {
            System.out.println("上传至数据库失败!");
            resp.sendRedirect("/onlineMusic/upload.html");
        }

     }
}
