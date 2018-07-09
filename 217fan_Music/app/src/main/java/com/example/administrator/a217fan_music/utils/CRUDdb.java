package com.example.administrator.a217fan_music.utils;

import android.util.Log;

import com.example.administrator.a217fan_music.bean.Music;
import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.List;

import static android.content.ContentValues.TAG;

public class CRUDdb {
    //创建数据库
    public void creatda(){
        LitePal.getDatabase();
        Log.w(TAG, "creatda: 数据库创建成功" );
    }
    public void upgrade(){
        LitePal.getDatabase();
        Log.w(TAG, "creatda: 数据库升级成功成功" );
    }
    public void incread(Music music){
        //Music music1=new Music(music.name,music.artist,music.songUri,music.albumUri,music.duration,
               // music.playedtime,music.isfavorite);
        music.save();
        Log.w(TAG, "incread: 数据添加成功" );
    }
    public void incread(List<Music> musicList){
            DataSupport.saveAll(musicList);
            Log.w(TAG, "incread: 数据添加成功" );
        }

    public void delete(Music music){
        DataSupport.deleteAll(Music.class,"name=? and artist= ?",music.name,music.artist);
        Log.w(TAG, "delete: 数据删除成功" );
    }
    public void delete(List<Music> musicList){
        for(Music music : musicList){
            DataSupport.deleteAll(Music.class,"name=? and artist= ?",music.name,music.artist);
            Log.w(TAG, "delete: 数据删除成功" );
        }
    }
    public List<Music> getmusicList(String songsheet){
        List<Music> musicList= DataSupport.where("songSheet=?",songsheet).find(Music.class);
        Log.w(TAG, "musicList: "+musicList.size() );
        Log.w(TAG, "getmusicList: 查询歌单歌曲数目" );
        return musicList;
    }
    public List<Music> getAllmusicList(){
        List<Music> musicList= DataSupport.findAll(Music.class);
        Log.w(TAG, "musicList: "+musicList.size() );
        Log.w(TAG, "getmusicList: 查询歌单歌曲数目" );
        return musicList;
    }

}
