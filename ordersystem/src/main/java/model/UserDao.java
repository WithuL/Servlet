package model;

import util.OrderSystemException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// 主要实现三个功能
// 1. 插入用户 - 注册的时候使用
// 2. 按名字查找用户 - 登陆时使用
// 3. 按照用户 id 查找 - 展示信息时使用
public class UserDao {
    public void add(User user) throws OrderSystemException {
        // [注意] 接下来的就是 JDBC 编程的基本流程了!!!!!
        // 1. 先获取和数据库的连接(DataSource)
        Connection connection = DBUtil.getConnection();
        // 2. 拼装 SQL 语句(PrepareStatement)
        String sql = "insert into user values(null, ?, ?, ?)";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, user.getName());
            statement.setString(2, user.getPassword());
            statement.setInt(3, user.getIsAdmin());
            // 3. 执行 SQL 语句(executeQuery, executeUpdate)
            int ret = statement.executeUpdate();
            if (ret != 1) {
                throw new OrderSystemException("插入用户失败");
            }
            System.out.println("插入用户成功!");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new OrderSystemException("插入用户失败");
        } finally {
            // 4. 关闭连接(close) (如果是查询语句, 还需要遍历结果集合)
            DBUtil.close(connection, statement, null);
        }
    }

    public User selectByName(String name) throws OrderSystemException {
        // 1. 获取数据库连接
        Connection connection = DBUtil.getConnection();
        // 2. 拼装 SQL
        String sql = "select * from user where name = ?";
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            // 3. 执行 SQL
            resultSet = statement.executeQuery();
            // 4. 遍历结果集(按照名字查找, 只能查到一个记录, 要求名字不能重复)
            if (resultSet.next()) {
                User user = new User();
                user.setUserId(resultSet.getInt("userId"));
                user.setName(resultSet.getString("name"));
                user.setPassword(resultSet.getString("password"));
                user.setIsAdmin(resultSet.getInt("isAdmin"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new OrderSystemException("按姓名查找用户失败");
        } finally {
            DBUtil.close(connection, statement, resultSet);
        }
        return null;
    }

    public User selectById(int userId) throws OrderSystemException {
        // 1. 获取数据库连接
        Connection connection = DBUtil.getConnection();
        // 2. 拼装 SQL
        String sql = "select * from user where userId = ?";
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            // 3. 执行 SQL
            resultSet = statement.executeQuery();
            // 4. 遍历结果集, userId 是主键. 不会重复的. 最多只能查到一条记录
            if (resultSet.next()) {
                User user = new User();
                user.setUserId(resultSet.getInt("userId"));
                user.setName(resultSet.getString("name"));
                user.setPassword(resultSet.getString("password"));
                user.setIsAdmin(resultSet.getInt("isAdmin"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new OrderSystemException("按 id 查找用户失败");
        } finally {
            // 5. 断开连接
            DBUtil.close(connection, statement, resultSet);
        }
        return null;
    }

    public static void main(String[] args) throws OrderSystemException {
        // 这个方法中进行 UserDao 的单元测试
        UserDao userDao = new UserDao();
        // 1. 验证插入数据
//        User user = new User();
//        user.setName("汤老湿");
//        user.setPassword("123456");
//        user.setIsAdmin(0);
//        userDao.add(user);
        // 2. 验证按照 名字 查找数据
//        User user = userDao.selectByName("汤老湿");
//        System.out.println("按照名字查找");
//        System.out.println(user);
//
//        user = userDao.selectById(2);
//        System.out.println("按照 id 查找");
//        System.out.println(user);
    }
}