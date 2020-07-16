package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
    //新增用户
    void add(User user) {
        Connection connection = DBUtil.getConnection();
        String sql = "insert into user values(null,?,?)";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, user.getName());
            statement.setString(2, user.getPassWord());
            int res = statement.executeUpdate();
            if(res != 1) {
                System.out.println("插入新用户失败");
                return;
            }
            System.out.println("插入新用户成功");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            DBUtil.close(connection, statement, null);
        }

    }

    public User selectByName(String name) {
        Connection connection = DBUtil.getConnection();
        String sql = "select * from user where name = ?";
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement("sql");
            statement.setString(1, name);
            //3.
            resultSet = statement.executeQuery();
            
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}

















