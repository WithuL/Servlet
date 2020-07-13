package Java_0713;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

public class ServletDemo2 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //处理表单的数据
        String Xing = req.getParameter("Xing");
        String Ming = req.getParameter("Ming");
        //构造相应
        resp.setContentType("text/html; charset=utf-8");
        Writer writer = resp.getWriter();
        writer.write("姓:"+Xing);
        writer.write("<br/>");
        writer.write("名:"+Ming);
        writer.write("<br/>");
    }
}
