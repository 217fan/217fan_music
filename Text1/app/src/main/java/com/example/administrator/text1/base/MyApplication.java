package com.example.administrator.text1.base;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import com.example.administrator.text1.bean.Music;

import org.litepal.LitePal;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MyApplication extends Application {
    List<Music>musicList;
    Context context;
    Music playmusic;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        LitePal.initialize(context);

        Log.w(TAG, "onCreate: myapplication创建了" );
    }
    public void setMusicList(List<Music>list){
        Log.w(TAG, "setMusicList: 设置全局变量成功" );
        this.musicList=list;
    }

    public List<Music> getMusicList() {
        if (musicList==null){
            Log.w(TAG, "getMusicList: 播放列表为空");
        }
        return musicList;
    }
    public void setMusic(Music playmusic){
        this.playmusic=playmusic;
    }

    public Music getPlaymusic() {
        if (playmusic==null) {
            return null;
        }else {
            return playmusic;
        }

    }

    public Context getContext() {
        return context;
    }
}
