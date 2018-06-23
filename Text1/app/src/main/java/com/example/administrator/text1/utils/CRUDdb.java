package com.example.administrator.text1.utils;

import android.util.Log;

import com.example.administrator.text1.bean.Music;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.List;

import javax.sql.DataSource;

import static android.content.ContentValues.TAG;
import static java.lang.System.in;

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
    public void incread(List<Music>musicList){
        for (Music music:musicList){
            music.save();
            Log.w(TAG, "incread: 数据添加成功" );
        }
    }
    public void update(Music music){


        music.updateAll("name=? and artist= ?",music.name,music.artist);
        if (music.playedtime==music.playedtime) {
            Log.w(TAG, "update: playedtime数据更新成功");
        }else  {
            Log.w(TAG, "update: playedtime数据更新失败");
        }
    }
    public void delete(Music music){
        DataSupport.deleteAll(Music.class,"name=? and artist= ?",music.name,music.artist);
        Log.w(TAG, "delete: 数据删除成功" );
    }
    public void delete(List<Music>musicList){
        for(Music music : musicList){
            DataSupport.deleteAll(Music.class,"name=? and artist= ?",music.name,music.artist);
            Log.w(TAG, "delete: 数据删除成功" );
        }
    }

}
