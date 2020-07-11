import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CalcServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String as = req.getParameter("a");
        String bs = req.getParameter("b");
        int a = Integer.parseInt(as);
        int b = Integer.parseInt(bs);
        int res = a + b;
        resp.getWriter().write(String.format("<h1>result = %d <h1>",res));
    }
}
