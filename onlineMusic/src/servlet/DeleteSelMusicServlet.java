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

@WebServlet("/deleteSelMusicServlet")
public class DeleteSelMusicServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");
        //删除所有选中的id
        //先获取所有的id
        String[] values = req.getParameterValues("id[]");
        //循环删除,并记录删除个数
        int count = 0;
        for(int i = 0;i < values.length; i++) {
            int id = Integer.parseInt(values[i]);
            MusicService musicService = new MusicService();
            Music music = musicService.findMusicById(id);
            //先判断在硬盘中是否有该音乐
            if(music != null) {
                //如果有的话就删除数据库信息
                int delete = musicService.deleteMusicById(id);
                if(delete == 1) {
                    System.out.println("数据库音乐删除成功");
                    //删除数据库成功后删除硬盘信息
                    File file = new File("/root/apache-tomcat-8.5.57/webapps/onlineMusic" + music.getUrl() + ".mp3");
                    //如果删除本地音乐成功
                    if(file.delete()) {
                        System.out.println("本地音乐删除成功!");
                        //就让删除数量++,否则就不+
                        count++;
                    }else {
                        System.out.println("本地音乐删除失败");
                    }
                }else {
                    System.out.println("本地音乐删除失败!");
                }
            }else {
                System.out.println("数据库中找不到该音乐");
            }

        }
        Map<String, Object> map = new HashMap<>();
        //开始写信息
        //如果申请删除歌曲的数量和实际删除的数量一致则返回true 否则 false
        if(count == values.length) {
            System.out.println("删除成功!");
            map.put("msg", true);
        }else {
            System.out.println("删除失败");
            map.put("msg", false);
        }
        System.out.println(count);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(resp.getWriter(), map);
    }
}
