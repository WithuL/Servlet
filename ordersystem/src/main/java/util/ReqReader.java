package util;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class ReqReader {
    //需要读取body
    //先读取整个body
    public static String readBody(HttpServletRequest request) throws UnsupportedEncodingException {
        //先去获取body的长度 单位是字节
        int length = request.getContentLength();
        byte[] buffer = new byte[length];
        try(InputStream inputStream = request.getInputStream()) {
            inputStream.read(buffer, 0 , length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  new String(buffer, "utf-8");
    }
}
