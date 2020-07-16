package java_0712;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Writer;

public class ServletDemo9 extends HttpServlet {
    //如果用户曾经没有访问过,就创建新的session
    //如果访问过了就获取以前的

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //这个操作会自动生成一个sessionId,同时创建一个新的session
        HttpSession httpSession = req.getSession(true);
        Integer count = 1;
        if(httpSession.isNew()) {
            httpSession.setAttribute("count",1);
        } else {
            count = (Integer) httpSession.getAttribute("count");
            httpSession.setAttribute("count", count+1);
        }
        resp.setContentType("text/html; charset=utf-8");
        Writer writer = resp.getWriter();
        writer.write("<html>");
        writer.write("您已访问:"+count+"次");
        writer.write("</html>");
    }
}

