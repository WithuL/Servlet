package java_0713;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;

@MultipartConfig
public class ServletDemo10 extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String basePath = "F:/Bit/tmp/";
        Part image =  req.getPart("image");
        String path = basePath + image.getSubmittedFileName();
        image.write(path);

        resp.setContentType("text/html; charset=utf-8");
        resp.getWriter().write("文件上传成功哦o(><；)oo");
    }
}
