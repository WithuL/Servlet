package servlet;

import entity.User;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import service.ReLogin;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

@WebServlet ("/upload")
public class UploadServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");

        User user = (User) req.getSession().getAttribute("user");
        if(user == null) {
            System.out.println("用户未登录!");
            resp.sendRedirect("/onlineMusic/login.html");
        }else {
            resp.setContentType("application/json;charset=utf-8");

            FileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);

            List<FileItem> fileItemList = null;

            try {
                fileItemList = upload.parseRequest(req);

            } catch (FileUploadException e) {
                e.printStackTrace();
                return;
            }
            System.out.println("fileItemList:"+fileItemList);
            FileItem fileItem = fileItemList.get(0);
            System.out.println("fileItem:"+fileItem);

           //1.把文件上传到服务器硬盘上 并且把歌名写道session里
            //sessions 对应的是 sessionId -> session
            //session 也是个哈希表, 里面保存了很多键值对
            //直接写入到session需要的信息就行了
            String fileName = fileItem.getName();
            req.getSession().setAttribute("fileName", fileName);
            try {
                fileItem.write(new File("/root/apache-tomcat-8.5.57/webapps/onlineMusic/music", fileName));
                System.out.println("上传至服务器成功!");

            } catch (Exception e) {
                System.out.println("上传到服务器失败");

                e.printStackTrace();
            }
            //2.把音乐上传到服务器数据库上
            resp.sendRedirect("/onlineMusic/uploadsucess.html");

        }
    }
}
