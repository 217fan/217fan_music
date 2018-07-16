package com.example.administrator.a217fan_music.utils;

import android.util.Log;

import com.example.administrator.a217fan_music.bean.Music;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class Http {
    public static List<Music> list=new ArrayList<Music>();
    public static Music music=new Music();
    static  String TAG="Http";
    public static boolean isFinishid=false;
    public static boolean isIsFinishUrl=false;
    public void getid_Json(String songname,okhttp3.Callback callback){
        isFinishid=false;
        OkHttpClient okHttpClient=new OkHttpClient();
        RequestBody requestBody=new FormBody.Builder().
                add("s",songname).
                add("type","1")
                .build();
        Request request=new Request.Builder()
                .url("http://music.163.com/api/search/pc")
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
        Log.w(TAG, "getid_Json: 创建连接" );
    }
    public void getSongUrl_Json(String id,okhttp3.Callback callback){
        isIsFinishUrl=false;
        OkHttpClient okHttpClient=new OkHttpClient();
        Request request=new Request.Builder()
                .url("http://music.163.com/api/song/enhance/player/url?"+id+"&ids=["+id+"]&br=3200000")
                .build();
        okHttpClient.newCall(request).enqueue(callback);
        Log.w(TAG, "getSongUrl_Json: 已成功连接" );
    }
}
