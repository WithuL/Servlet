package dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import entity.Music;
import util.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MusicDao {
    //查询全部歌单
    public List<Music> findMusic() {
        List<Music> musics = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        connection = DBUtils.getConnection();
        try {
            preparedStatement = connection.prepareStatement("select * from music");
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                Music music = new Music();
                music.setId(resultSet.getInt("id"));
                music.setTitle(resultSet.getString("title"));
                music.setSinger(resultSet.getString("singer"));
                music.setTime(resultSet.getDate("time"));
                music.setUrl(resultSet.getString("url"));
                music.setUserid(resultSet.getInt("userid"));
                musics.add(music);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.getClose(connection, preparedStatement, resultSet);
        }
        return musics;
    }
    //根据id查找音乐
    public Music findMusicById(int id) {
        Music music = null;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement("select * from music where id = ?");
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                music = new Music();
                music.setId(resultSet.getInt("id"));
                music.setTitle(resultSet.getString("title"));
                music.setSinger(resultSet.getString("singer"));
                music.setTime(resultSet.getDate("time"));
                music.setUrl(resultSet.getString("url"));
                music.setUserid(resultSet.getInt("userid"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.getClose(connection, statement, resultSet);
        }
        return music;
    }
    //根据关键词查找音乐
    public List<Music> ifMusic(String str ) {
        List<Music> musics = new ArrayList<>();
        Connection connection = DBUtils.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement("select * from Music where title like '%" + str + "%'");
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Music music = new Music();
                music.setId(resultSet.getInt("id"));
                music.setTitle(resultSet.getString("title"));
                music.setSinger(resultSet.getString("singer"));
                music.setTime(resultSet.getDate("time"));
                music.setUrl(resultSet.getString("url"));
                music.setUserid(resultSet.getInt("userid"));
                musics.add(music);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.getClose(connection, preparedStatement, null);
        }
        return musics;
    }
    //上传音乐
    public int insert(String title , String singer, String time, String url, int userid) {
        Connection connection = DBUtils.getConnection();
        PreparedStatement preparedStatement = null;
        int  number = 0;
        try {
            String str = "insert into music(title, singer, time, url, userid) values(?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(str);
            preparedStatement.setString(1,title);
            preparedStatement.setString(2,singer);
            preparedStatement.setString(3,time);
            preparedStatement.setString(4,url);
            preparedStatement.setInt(5,userid);
            number = preparedStatement.executeUpdate();
            return number;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.getClose(connection, preparedStatement, null);
        }
        return 0;
    }
    //删除数据库的歌曲
    public int deleteMusicById(int id) {
        Connection connection = DBUtils.getConnection();
        PreparedStatement preparedStatement = null;
        int ret = 0;
        try {
            preparedStatement = connection.prepareStatement("delete from music where id = ?");
            preparedStatement.setInt(1,id);
            ret = preparedStatement.executeUpdate();
            if(ret == 1) {
                if(findLoveMusicOnDel(id)) {
                    int ret2 = removeLoveMusicOnDelete(id);
                    if(ret2 == 1) {
                        return 1;
                    }
                } else {
                    return 1;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    //删除音乐时查看是否为喜欢的音乐
    private boolean findLoveMusicOnDel(int music_id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            String str = "select * from lovemusic where music_id = ?";
            connection = DBUtils.getConnection();
            preparedStatement = connection.prepareStatement(str);
            preparedStatement.setInt(1, music_id) ;
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.getClose(connection, preparedStatement, resultSet);
        }
        return false;
    }
    //移除中间表中的某首歌
    private int removeLoveMusicOnDelete(int music_id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DBUtils.getConnection();
            String str = "delete from lovemusic where music_id = ?";
            preparedStatement = connection.prepareStatement(str);
            preparedStatement.setInt(1, music_id);
            int ret =preparedStatement.executeUpdate();
            if (ret == 1) {
                return ret;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.getClose(connection, preparedStatement, null);
        }
        return 0;
    }
    //添加音乐到喜欢的音乐列表中
    public boolean insertLoveMusic(int userid, int musicid) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DBUtils.getConnection();
            String str = "insert into lovemusic(user_id, music_id) values(?,?)";
            preparedStatement = connection.prepareStatement(str);
            preparedStatement.setInt(1, userid);
            preparedStatement.setInt(2,musicid);
            int ret = preparedStatement.executeUpdate();

            if(ret == 1) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.getClose(connection, preparedStatement ,null);
        }
        return false;
    }
    //移除某位用户喜欢的音乐
    public int removeLoveMusic(int userid,int musicid) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DBUtils.getConnection();
            String str = "delete from lovemusic where user_id = ? and music_id = ?";
            preparedStatement = connection.prepareStatement(str);
            preparedStatement.setInt(1,userid);
            preparedStatement.setInt(2,musicid);
            int ret = preparedStatement.executeUpdate();
            if(ret == 1) {
                return ret;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.getClose(connection, preparedStatement, null);
        }
        return 0;
    }
    //添加喜欢的音乐的时候需要判断该音乐是否已在喜欢的列表中
    public boolean findMusicByMusicId(int userid, int musicid) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ResultSet resultSet1 = null;
        try {
            connection = DBUtils.getConnection();
            String str = "select * from lovemusic where user_id = ? and music_id = ?";
            preparedStatement = connection.prepareStatement(str);
            preparedStatement.setInt(1, userid);
            preparedStatement.setInt(2,musicid);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.getClose(connection, preparedStatement , resultSet);
        }
        return false;
    }


    //查看某位用户喜欢的音乐
    public List<Music> findLoveMusic(int userid) {
        List<Music> musics = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DBUtils.getConnection();
            String str = "select * from music where id in(select music_id from lovemusic where user_id = ?)";
            statement = connection.prepareStatement(str);
            statement.setInt(1, userid);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Music music = new Music();
                music.setId(resultSet.getInt("id"));
                music.setTitle(resultSet.getString("title"));
                music.setSinger(resultSet.getString("singer"));
                music.setTime(resultSet.getDate("time"));
                music.setUrl(resultSet.getString("url"));
                music.setUserid(resultSet.getInt("userid"));
                musics.add(music);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.getClose(connection, statement, resultSet);
        }
        return musics;
    }

    //根据关键字查询喜欢的歌单
    public List<Music> ifMusicLove(String str, int userid) {
        List<Music> musics = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DBUtils.getConnection();
            statement = connection.prepareStatement("select * from music where title like '%" + str +"%'" + "and id in" +
                    "(select music_id from lovemusic where user_id = ? )");
            statement.setInt(1,userid);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Music music = new Music();
                music.setId(resultSet.getInt("id"));
                music.setTitle(resultSet.getString("title"));
                music.setSinger(resultSet.getString("singer"));
                music.setTime(resultSet.getDate("time"));
                music.setUrl(resultSet.getString("url"));
                music.setUserid(resultSet.getInt("userid"));
                musics.add(music);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.getClose(connection, statement, resultSet);
        }
        return musics;
    }


    public List<Music> findMusicBySingerName(String singer) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Music> musics = null;
        try {
            connection = DBUtils.getConnection();
            String sql = "select * from Music where singer like '%"+singer+"%'";
            statement =connection.prepareStatement(sql);
            resultSet = statement.executeQuery();
            while(resultSet.next()) {
                Music music = new Music();
                music.setId(resultSet.getInt("id"));
                music.setTitle(resultSet.getString("title"));
                music.setSinger(resultSet.getString("singer"));
                music.setTime(resultSet.getDate("time"));
                music.setUrl(resultSet.getString("url"));
                music.setUserid(resultSet.getInt("userid"));
                musics.add(music);
                System.out.println("有结果");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.getClose(connection, statement, resultSet);
        }
        return musics;
    }
}
