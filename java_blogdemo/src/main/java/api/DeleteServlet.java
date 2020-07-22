package api;

import model.Article;
import model.ArticleDao;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class DeleteServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=utf-8");
        HttpSession httpSession = req.getSession();
        if(httpSession == null) {
            String html = HtmlGenerator.getMessagePage("您尚未登陆!",
                    "login.html");
            resp.getWriter().write(html);
            return;
        }
        User user = (User) httpSession.getAttribute("user");
        String articleId = req.getParameter("articleId");
        if(articleId == null || "".equals(articleId)) {
            String html = HtmlGenerator.getMessagePage("要删除的文章 id 有误",
                    "article");
            resp.getWriter().write(html);
            return;
        }
        ArticleDao  articleDao = new ArticleDao();
        Article article = articleDao.selectByid(Integer.parseInt(articleId));
        if(article.getUserId() != user.getUserId()) {
            String html = HtmlGenerator.getMessagePage("您只能删除自己的文章!",
                    "article");
            resp.getWriter().write(html);
            return;
        }
        articleDao.delete(Integer.parseInt(articleId));
        String html = HtmlGenerator.getMessagePage("删除成功!",
                "article");
        resp.getWriter().write(html);
    }
}
