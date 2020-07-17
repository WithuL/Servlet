package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ArticleDao {
    public void add(Article article) {
        Connection connection = DBUtil.getConnection();
        String sql = "insert into article values(null,?,?,?)";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, article.getTitle());
            statement.setString(2, article.getContent());
            statement.setInt(3, article.getUserId());
            int res = statement.executeUpdate();
            if(res != 1) {
                System.out.println("插入失败!");
                return;
            }
            System.out.println("插入成功");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            DBUtil.close(connection, statement, null);
        }
    }

    public List<Article> selectAll() {
        Connection connection = DBUtil.getConnection();
        String sql = "select articleId, title, userId from article ";
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Article> articles = new ArrayList<Article>();
        try {
            statement = connection.prepareStatement("sql");
            resultSet = statement.executeQuery();
            while(resultSet.next()) {
                Article article = new Article();
                article.setArticleId(resultSet.getInt("articleId"));
                article.setTitle(resultSet.getString("title"));
                article.setUserId(resultSet.getInt("userId"));
                articles.add(article);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }  finally {
            DBUtil.close(connection, statement, resultSet);
        }
        return null;
    }

    public Article selectByid(int articleId) {
        Connection connection = DBUtil.getConnection();
        String sql = "select * from article where articleId = ?";
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement  = connection.prepareStatement(sql);
            statement.setInt(1, articleId);
            resultSet = statement.executeQuery();
            while(resultSet.next()) {
                Article article = new Article();
                article.setArticleId(resultSet.getInt("articleId"));
                article.setTitle(resultSet.getString("title"));
                article.setContent(resultSet.getString("content"));
                article.setUserId(resultSet.getInt("userId"));
                return article;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            DBUtil.close(connection, statement, resultSet);
        }
        return null;
    }

    public void delete(int articleId) {
        Connection connection = DBUtil.getConnection();
        String sql = "delete from article where articleId = ?";
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(sql);
            statement.setInt(1, articleId);
            int res = statement.executeUpdate();
            if(res != 1) {
                System.out.println("删除失败");
                return;
            }
            System.out.println("删除成功");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            DBUtil.close(connection, statement, null);
        }
    }

    public static void main(String[] args) {
        ArticleDao articleDao = new ArticleDao();
        articleDao.delete(3);
        articleDao.delete(4);
        articleDao.delete(5);
    }
}
