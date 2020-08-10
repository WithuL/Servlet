package model;

import com.sun.org.apache.xpath.internal.operations.Or;
import util.OrderSystemException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
    public void add(User user) throws OrderSystemException {
        Connection connection = DBUtil.getConnection();
        PreparedStatement preparedStatement = null;
        String sql = "insert into user values(null,?,?,?)";
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,user.getName());
            preparedStatement.setString(2,user.getPassword());
            preparedStatement.setInt(3,user.getIsAdmin());
            int ret = preparedStatement.executeUpdate();
            if(ret != 1) {
                throw new OrderSystemException("用户插入失败");
            }
            System.out.println("用户插入成功");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            DBUtil.getClose(connection, preparedStatement, null);
        }
    }

    public User selectByName(String name) throws OrderSystemException {
        Connection connection = DBUtil.getConnection();
        PreparedStatement preparedStatement = null;
        String sql = "select * from user where name = ?";
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("userid"));
                user.setName(resultSet.getString("name"));
                user.setPassword(resultSet.getString("password"));
                user.setIsAdmin(resultSet.getInt("isAdmin"));
                return user;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new OrderSystemException("按姓名查找用户失败");
        } finally {
            DBUtil.getClose(connection, preparedStatement, resultSet);
        }
        return  null;
    }

    public User selectById(int userid) throws OrderSystemException {
        Connection connection = DBUtil.getConnection();
        PreparedStatement preparedStatement = null;
        String sql = "select * from user where userid = ?";
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userid);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("userid"));
                user.setName(resultSet.getString("name"));
                user.setPassword(resultSet.getString("password"));
                user.setIsAdmin(resultSet.getInt("isAdmin"));
                return user;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new OrderSystemException("按姓名查找用户失败");
        } finally {
            DBUtil.getClose(connection, preparedStatement, resultSet);
        }
        return  null;
    }

    public static void main(String[] args) throws OrderSystemException {
        UserDao userDao = new UserDao();
        User user = userDao.selectById(1);
        System.out.println(user);
    }
}
