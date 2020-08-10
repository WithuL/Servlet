package model;

import com.sun.org.apache.xpath.internal.operations.Or;
import javafx.animation.ParallelTransition;
import util.OrderSystemException;

import javax.print.attribute.standard.PresentationDirection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDao {

// 操作订单
// 1. 新增订单
// 2. 查看所有订单(管理员, 商家)
// 3. 查看指定用户的订单(普通用户, 顾客)
// 4. 查看指定订单的详细信息
// 5. 修改订单状态(订单是否已经完成)

    public void add(Order order) throws OrderSystemException {
        addOrderUser(order);
        addOderDish(order);
    }

    private void addOrderUser(Order order) throws OrderSystemException {
        Connection connection = DBUtil.getConnection();
        PreparedStatement preparedStatement = null;
        String sql = "insert into orderuser values(null ,?,now(),0)";
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1,order.getUserid());
            int ret = preparedStatement.executeUpdate();
            if(ret != 1) {
                throw new OrderSystemException("插入订单失败");
            }
            //把自增主键读出来
            resultSet = preparedStatement.getGeneratedKeys();
            if(resultSet.next()) {
                order.setOrderid(resultSet.getInt(1));
            }
            System.out.println("插入第一步成功");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new OrderSystemException("插入订单失败");
        } finally {
            DBUtil.getClose(connection, preparedStatement, null);
        }
    }

    private void addOderDish(Order order) throws OrderSystemException {
        Connection connection = DBUtil.getConnection();
        PreparedStatement preparedStatement = null;
        String sql = "insert into orderuser values(?,?)";

        try {
            //关闭自动提交    默认是自动提交的,调用execute就自动执行了,把请求sql发送给服务器了
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement(sql);
            List<Dish> dishes = order.getDishes();
            for(Dish dish : dishes) {
                preparedStatement.setInt(1,order.getOrderid());
                preparedStatement.setInt(2,dish.getDishid());
                preparedStatement.addBatch();//给sql新增一组values片段
            }
            preparedStatement.executeBatch();//进行统一执行sql,但不是真正执行
            connection.commit();//真正执行(发送给服务器)
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            //如果前面的结果出错就进行回滚,即删除操作
            deleteOrderUser(order.getOrderid());
        } finally {
            DBUtil.getClose(connection, preparedStatement,null);
        }
    }

    private void deleteOrderUser(int orderid) throws OrderSystemException {
        Connection connection = DBUtil.getConnection();
        PreparedStatement preparedStatement = null;
        String sql = "delete from orderuser where orderid = ?";

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,orderid);
            int ret = preparedStatement.executeUpdate();
            if(ret != 1) {
                throw new OrderSystemException("删除订单用户表失败!");
            }
            System.out.println("回滚成功!");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new OrderSystemException("回滚失败!");
        } finally {
            DBUtil.getClose(connection, preparedStatement, null);
        }
    }

    // 获取到所有的订单信息
    // Order 对象里, 有一些 orderId, userId 这些属性. 直接借助 order_user 表就获取到了
    // 还有一个重要的属性, dishes (List<Dish>) .
    // 详细信息需要先根据 order_dish 表, 获取到 所有的相关的 dishId, 然后在根据 dishId
    // 去 dishes 表中查.
    // 仔细思考, 可以发现, 这里的订单获取, 不需要获取那么详细的内容. 只获取到订单的一些基本信息就行了.
    // 菜品信息, 反正有一个查看指定订单详细信息的接口
    // 当前这个接口返回的 Order 对象中, 不包含 dishes 详细数据的.
    // 这样做是为了让代码更简单, 更高效
    public List<Order> selectAll() {
        //1.先获取数据库的链接
        List<Order> orders = new ArrayList<Order>();
        Connection connection = DBUtil.getConnection();
        PreparedStatement preparedStatement = null;
        //2.拼装sql
        String sql = "select * from orderuser";
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            //执行sql
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                Order order = new Order();
                order.setOrderid(resultSet.getInt("orderid"));
                order.setUserid(resultSet.getInt("userid"));
                order.setTime(resultSet.getTimestamp("time"));
                order.setIsdone(resultSet.getInt("isdone"));
                orders.add(order);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            DBUtil.getClose(connection, preparedStatement, resultSet);
        }
        return orders;
    }

    //查询某位用户的订单
    public List<Order> selectByUserId(int userId) {
        List<Order> orders = new ArrayList<Order>();
        Connection connection = DBUtil.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "select * from orderuser where userid = ?";

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                Order order = new Order();
                order.setOrderid(resultSet.getInt("orderid"));
                order.setUserid(resultSet.getInt("userid"));
                order.setTime(resultSet.getTimestamp("time"));
                order.setIsdone(resultSet.getInt("isdone"));
                orders.add(order);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            DBUtil.getClose(connection, preparedStatement, resultSet);
        }
        return orders;
    }

    //这个方法就要把order对象完整的写进去
    // 包括 Order 中有哪些的菜品, 以及菜品的详情
    // [注意] 此处的操作, 咱们使用的是多次查询的方式完成的.
    // 除此之外, 也可以使用多表查询来完成. (sql 语句会更复杂, 当时 java 代码会更简单一些)
    public Order selectById(int orderid) throws OrderSystemException {
        //1.首先根据一个orderid得到一个order对象,
        Order order = buildOrder(orderid);
        //2.根据orderid得到orderid对应的菜品id列表
        List<Integer> dishids = selectDishIds(orderid);
        //3.根据id列表查询dishes表,获取菜品详情
        order = getDishDetail(order, dishids);
        return order;
    }

    private Order buildOrder(int orderid) {
        // 1. 获取到数据库连接
        Connection connection = DBUtil.getConnection();
        // 2. 拼装 SQL
        String sql = "select * from order_user where orderid = ?";
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setInt(1, orderid);
            // 3. 执行 SQL
            resultSet = statement.executeQuery();
            // 4. 遍历结果集合
            if (resultSet.next()) {
                Order order = new Order();
                order.setOrderid(resultSet.getInt("orderid"));
                order.setUserid(resultSet.getInt("userid"));
                order.setTime(resultSet.getTimestamp("time"));
                order.setIsdone(resultSet.getInt("isdone"));
                return order;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, statement, resultSet);
        }
        return null;
    }

    private List<Integer> selectDishIds(int orderid) {
        List<Integer> dishIds = new ArrayList<Integer>();
        Connection connection = DBUtil.getConnection();
        String sql = "select * from order_dish where orderid = ?";
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setInt(1, orderid);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                dishIds.add(resultSet.getInt("dishid"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, statement, resultSet);
        }
        return dishIds;
    }

    private Order getDishDetail(Order order, List<Integer> dishids) throws OrderSystemException {
        List<Dish> dishes = new ArrayList<Dish>();
        DishDao dishDao = new DishDao();
        for(int x : dishids) {
            Dish dish = dishDao.selectById(x);
            dishes.add(dish);
        }
        order.setDishes(dishes);
        return order;
    }

    //修改订单状态
    public void changeState(int orderid, int isdone) throws OrderSystemException {
        Connection connection = DBUtil.getConnection();
        PreparedStatement preparedStatement = null;
        String sql = "update orderuser set isdone=? where orderid = ?";

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, orderid);
            preparedStatement.setInt(2, isdone);
            int ret = preparedStatement.executeUpdate();
            if(ret != 1) {
                throw new OrderSystemException("修改订单状态失败");
            }
            System.out.println("修改订单状态成功!");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            DBUtil.getClose(connection, preparedStatement,null);
        }
    }
}
