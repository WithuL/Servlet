package model;

import com.sun.org.apache.bcel.internal.generic.RETURN;
import com.sun.org.apache.xpath.internal.operations.Or;
import util.OrderSystemException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//1.新增菜品
//2.删除菜品
//3.查询所有菜品
//4.查询指定菜品
//
public class DishDao {
    public void add(Dish dish) throws OrderSystemException {
        Connection connection = DBUtil.getConnection();
        PreparedStatement preparedStatement = null;

        try {
            String sql = "insert into dishes values(null,?,?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,dish.getName());
            preparedStatement.setInt(2,dish.getPrice());
            int ret = preparedStatement.executeUpdate();
            if(ret != 1) {
                throw new OrderSystemException("插入菜品失败");
            }
            System.out.println("插入菜品成功");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new OrderSystemException("插入菜品失败");

        } finally {
            DBUtil.getClose(connection, preparedStatement, null);
        }
    }

    public void delete(int dishid) throws OrderSystemException {
        Connection connection = DBUtil.getConnection();
        PreparedStatement preparedStatement = null;
        try {
            String sql = "delete from dishes where dishid = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,dishid);
            int ret = preparedStatement.executeUpdate();
            if(ret != 1) {
                throw new OrderSystemException("删除菜品失败");
            }
            System.out.println("删除菜品成功");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new OrderSystemException("删除菜品失败");
        } finally {
            DBUtil.getClose(connection, preparedStatement, null);
        }
    }

    public List<Dish> selectAll() throws OrderSystemException {
        Connection connection = DBUtil.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Dish> dishes = new ArrayList<Dish>();
        try {
            String sql = "select * from dishes";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                Dish dish = new Dish();
                dish.setDishid(resultSet.getInt("dishid"));
                dish.setName(resultSet.getString("name"));
                dish.setPrice(resultSet.getInt("price"));
                dishes.add(dish);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new OrderSystemException("查询菜单失败");
        } finally {
            DBUtil.getClose(connection, preparedStatement, resultSet);
        }
        return dishes;
    }

    public Dish selectById(int dishid) throws OrderSystemException {
        Connection connection = DBUtil.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "select * from dishes where dishid = ?";
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,dishid);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                Dish dish = new Dish();
                dish.setDishid(resultSet.getInt("dishid"));
                dish.setName(resultSet.getString("name"));
                dish.setPrice(resultSet.getInt("price"));
                return dish;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new OrderSystemException("查询菜单失败");
        } finally {
            DBUtil.getClose(connection, preparedStatement, resultSet);
        }
        return null;
    }

    public static void  main(String[] args) throws OrderSystemException {
        DishDao dishDao = new DishDao();
        Dish dish = dishDao.selectById(2);
        System.out.println(dish);
    }
}
