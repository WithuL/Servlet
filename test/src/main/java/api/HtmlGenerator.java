package api;

import model.Article;
import model.User;

import java.util.List;

public class HtmlGenerator {
    //通过字符串拼接,构造出一个html的格式得到
    public static String getMessagePage(String message, String nextUrl) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("<head>");
        sb.append("<meta charset=\"utf-8\">");
        sb.append("<title>提示页面</title>");
        sb.append("</head>");
        sb.append("<body>");
        sb.append("<h3>");
        sb.append(message);
        sb.append("</h3>");
        //跳转
        sb.append(String.format("<a href=\"%s\" >点击这里跳转</a>",nextUrl));
        sb.append("</body>");
        sb.append("</html>");
        return sb.toString();
    }

    public static String getArticleListPage(List<Article> articles, User user) {
        //查看文件列表
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("<head>");
        sb.append("<meta charset=\"utf-8\">");
        sb.append("<title>文章列表</title>");
        sb.append("</head>");
        sb.append("<body>");

        sb.append("<h3> 欢迎您! " + user.getName() + "</h3>");
        sb.append("<hr>");
        if(articles == null) {
            sb.append("<div><h3>");
            sb.append("您当前没有文章哦");
            sb.append("</h3></div>");
        }else {
            for(Article article : articles) {
                sb.append(String.format("<div> <a class=\"article\" href=\"article?articleId=%d\"> %s </a>" +
                                "<a href=\"delteArticle?aricleId=%d\"> 删除 </a></div>" ,
                        article.getArticleId(), article.getTitle(),  article.getArticleId()));
            }
            sb.append(String.format("<div>当前共有博客 %d 篇</div>", articles.size()));
        }
        //发布文章
        // 在这里新增发布文章的区域
        sb.append("<div> 发布文章 </div>");
        sb.append("<div>");
        sb.append("<form method=\"post\" action=\"article\">");
        sb.append("<input type=\"text\" style=\"width: 500px; margin-bottom: 5px;\" name=\"title\" placeholder=\"请输入标题\">");
        sb.append("<br>");
        sb.append("<textarea name=\"content\" style=\"width: 500px; height: 300px;\"></textarea>");
        sb.append("<br>");
        sb.append("<input type=\"submit\" value=\"发布文章\">");
        sb.append("</form>");
        sb.append("</div>");

        sb.append("</body>");
        sb.append("</html>");
        return sb.toString();
    }

    public static String getArticleDetailPage(Article article, User user, User author) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("<head>");
        sb.append("<meta charset=\"utf-8\">");
        sb.append("<title>文章详情</title>");
        sb.append("</head>");
        sb.append("<body>");

        sb.append("<h3> 欢迎您! " + user.getName() + "</h3>");

        sb.append("<hr>");
        sb.append(String.format("<h1>%s</h1>", article.getTitle()));
        sb.append(String.format("<h4>作者: %s </h4>", author.getName()));
        sb.append("<hr>");

        sb.append(String.format( "<div>%s</div>", article.getContent().replace("\n", "<br>")));
        sb.append("</body>");
        sb.append("</html>");

        //发布文章
        return sb.toString();
    }
}
