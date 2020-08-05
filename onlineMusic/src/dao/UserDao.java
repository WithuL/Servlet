package dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.User;
import util.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
    public User login(User loginUser) {
        User user = null;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        connection  = DBUtils.getConnection();
        try {
            statement = connection.prepareStatement("select * from user where username = ? and password = ?");
            statement.setString(1, loginUser.getUserName());
            statement.setString(2, loginUser.getPassword());
            resultSet = statement.executeQuery();
            while(resultSet.next()) {
                user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUserName(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                user.setAge(resultSet.getInt("age"));
                user.setGender(resultSet.getString("gender"));
                user.setEmail(resultSet.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.getClose(connection, statement, resultSet);
        }
        return user;
    }

    public void inserUser(User newUser) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        connection = DBUtils.getConnection();
        try {
            preparedStatement = connection.prepareStatement("select * from user where username = ?");
            preparedStatement.setString(1, newUser.getUserName());
            ResultSet l = preparedStatement.executeQuery();
            if(l.next()) {
                System.out.println("用户名已存在!");
                return;
            }
            preparedStatement = connection.prepareStatement("insert into user values(null,?,?,?,?,?)");
            preparedStatement.setString(1, newUser.getUserName());
            preparedStatement.setString(2, newUser.getPassword());
            preparedStatement.setInt(3, newUser.getAge());
            preparedStatement.setString(4, newUser.getGender());
            preparedStatement.setString(5, newUser.getEmail());
            preparedStatement.executeUpdate();
            System.out.println("插入成功!");
        } catch (SQLException e) {
            System.out.println("插入失败");
            e.printStackTrace();
        } finally {
            DBUtils.getClose(connection, preparedStatement, null);
        }

    }

    public int ifUser(String username){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DBUtils.getConnection();
            String sql = "select id from user where username = '" + username + "'";
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();
            if(resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.getClose(connection, statement, resultSet);
        }
        return 0;
    }
    public static void main(String[] args) throws JsonProcessingException {
        UserDao userDao = new UserDao();
        User user = new User();
        user.setUserName("cb");
        user.setPassword("123");
        user.setGender("女");
        user.setAge(20);
        user.setEmail("1509586319@qq.com");
        userDao.inserUser(user);
    }
}
