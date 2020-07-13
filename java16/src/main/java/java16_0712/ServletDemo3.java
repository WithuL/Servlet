package java16_0712;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.Enumeration;

public class ServletDemo3 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Enumeration<String> headerNames = req.getHeaderNames();
        resp.setContentType("text/html; charset=utf-8");

        Writer writer = resp.getWriter();
        writer.write("<html>");
        while(headerNames.hasMoreElements()) {
            String header = headerNames.nextElement();
            writer.write(header+":"+req.getHeader(header));
            writer.write("<br/>");
        }
        writer.write("</html>");
    }
}
