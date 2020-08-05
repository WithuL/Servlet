package service;

import dao.MusicDao;
import entity.Music;

import java.util.List;

public class MusicService {
    private MusicDao musicDao = new MusicDao();

    //找到所有的音乐

    public List<Music> findMusic() {
        return musicDao.findMusic();
    }

    //根据ID查找音乐
    public Music findMusicById(int id) {
        return musicDao.findMusicById(id);
    }

    //根据关键词查询音乐
    public List<Music> ifMusic(String str) {
        return musicDao.ifMusic(str);
    }

    //上传音乐
    public int insert(String title , String singer, String time, String url, int userid) {
        return musicDao.insert(title , singer, time, url, userid);
    }

    //根据ID删除数据库中的音乐
    public int deleteMusicById(int id) {
        return musicDao.deleteMusicById(id);
    }

    //添加音乐到喜欢的列表
    public boolean insertLoveMusic(int userid, int musicid) {
        return musicDao.insertLoveMusic(userid, musicid);
    }
    //添加到喜欢的列表时判断是否已在列表中
    public boolean findMusicByMusicId(int userid, int musicid) {
        return musicDao.findMusicByMusicId(userid, musicid);
    }

    //移除用户喜欢的音乐
    public int removeLoveMusic(int userid, int musicid) {
        return musicDao.removeLoveMusic(userid, musicid);
    }

    //查看某位用户喜欢的音乐列表
    public List<Music> findLoveMusic(int userid) {
        return musicDao.findLoveMusic(userid);
    }

    //根据关键字查询喜欢的音乐
    public List<Music> ifMusicLove(String str, int userid) {
        return musicDao.ifMusicLove(str, userid);
    }

    public List<Music> findMusicBySingerName(String singer) {
        return musicDao.findMusicBySingerName(singer);
    }
}
