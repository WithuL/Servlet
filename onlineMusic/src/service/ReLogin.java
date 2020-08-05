package service;

public class ReLogin {
    public static String relogin() {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("<head>");
        sb.append("<meta charset=\"utf-8\">");
        sb.append("</head>");
        sb.append("<body>");
        sb.append("<h3>请登陆后再使用</h3>");
        sb.append("<a href=\"/login.html\">点击此处登录</a>");
        sb.append("</body>");
        sb.append("</html>");
        return sb.toString();
    }
}
