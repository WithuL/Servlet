package java_0712;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

public class ServletDemo2 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String firstName = req.getParameter("firstName");
        String secondName = req.getParameter("secondName");
        resp.setContentType("text/html; charset=utf-8");

        Writer writer = resp.getWriter();
        writer.write("<html>");
        writer.write("<br/>");
        writer.write("firstName:"+firstName);
        writer.write("<br/>");
        writer.write("secondName:"+secondName);
        writer.write("<br/>");
        writer.write("测试");
        writer.write("</html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
