import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TestMySql {
    public static void main(String[] args) throws SQLException {
        //1.设置数据库参数
        String url = "jdbc:mysql://127.0.0.1:3306/test1?characterEncoding=utf-8&useSSL=true";
        DataSource dataSource = new MysqlDataSource();
        String userName = "root";
        String passWord = "aytdd@2@";
        ((MysqlDataSource)dataSource).setURL(url);
        ((MysqlDataSource)dataSource).setUser(userName);
        ((MysqlDataSource)dataSource).setPassword(passWord);
        //2.和数据库建立连接
        Connection connection = dataSource.getConnection();
        //3.访问数据库
        String sql =  "select * from student";
        PreparedStatement statement = connection.prepareStatement(sql);
        //4.执行sql
        ResultSet resultSet = statement.executeQuery();
        //5.遍历结果集
        //每次调用next方法就能获取一条记录
        //进一步使用get方法根据列名获取到对应的列的数据
        while(resultSet.next()) {
            System.out.print(resultSet.getInt("id"));
            System.out.print(resultSet.getString("name"));
            System.out.println(resultSet.getInt("english"));
        }
        //6.释放资源
        resultSet.close();
        statement.close();
        connection.close();

    }
}
