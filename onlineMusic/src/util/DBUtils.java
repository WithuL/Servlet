package util;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
public class DBUtils {
    private static String url = "jdbc:mysql://127.0.0.1:3306/musicserver?useSSL=false";
    private static String user = "root";
    private static String password = "";

    private static volatile DataSource dataSource ;

    public static DataSource getDataSource () {
        if(dataSource == null) {
            synchronized (DBUtils.class) {
                if(dataSource == null) {
                    dataSource = new MysqlDataSource();
                    ((MysqlDataSource)dataSource).setUrl(url);
                    ((MysqlDataSource)dataSource).setUser(user);
                    ((MysqlDataSource)dataSource).setPassword(password);
                }
            }
        }
        return dataSource;
    }

    public static Connection getConnection() {
        try {
            Connection connection = getDataSource().getConnection();
            return connection;
        } catch (SQLException throwables) {
            System.out.println("获取数据库连接失败!");
            throwables.printStackTrace();
        }
        return null;
    }

    public static void getClose(Connection connection , PreparedStatement statement , ResultSet resultSet) {
        if(resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if(statement != null) {
            try {
                statement.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if(connection != null) {
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
