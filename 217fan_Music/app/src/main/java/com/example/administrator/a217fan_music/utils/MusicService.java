package com.example.administrator.a217fan_music.utils;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.administrator.a217fan_music.activity.MainActivity;
import com.example.administrator.a217fan_music.bean.Music;


import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MusicService extends Service {
   static List<Music> musicList;
   static   Music playmusic;
    CRUDdb cruddb;
    MessageEvent messageEvent;
    MediaPlayer mediaPlayer;
    private static int Isplay=0;
    Uri songUri;
    //Isplay=0，是初始状态，=1是播放状态，=2是播放后暂停状态
    @Override
    public void onCreate() {
        super.onCreate();
        Log.w("MusicService", "onCreate: MusicService成功" );
        //实例化MessageEvent
        messageEvent=new MessageEvent();
        //建立前台服务
      NotificationCompat.Builder builder=new NotificationCompat.Builder(getBaseContext());
        Intent intent=new Intent(this,MainActivity.class);
        PendingIntent pi= PendingIntent.getActivity(this,0,intent,0);
        Notification notification=new NotificationCompat.Builder(this,"channerId")
                .setContentTitle("这是前台服务的头")
                .setContentText("这是前台服务的内容")
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pi)
                .build();
        startForeground(1,notification);
        Log.w(TAG, "onCreate: 前台服务已开启" );
        mediaPlayer=new MediaPlayer();
        mediaPlayer.setOnCompletionListener(onCompletionListener);

        //载入播放列表
        cruddb=new CRUDdb();
        List<Music>list=cruddb.getmusicList("本地音乐");
        musicList=list;
        Log.w(TAG, "onCreate: 服务启动成功" );
    }
    private MediaPlayer.OnCompletionListener onCompletionListener=new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            Log.w(TAG, "onCompletion: 监听器执行了" );
            playnext();

        }
    };
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer!=null&&mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer=null;
        }
    }
    private  IBinder musicBinder=new MusicBinder();
    @Override
    public IBinder onBind(Intent intent) {
        return musicBinder;
    }
    public class MusicBinder extends Binder {
       public MusicService getService(){
            return MusicService.this;
        }
    }
    public void playnext(){
       int position=musicList.indexOf(playmusic);
        Log.w(TAG, "playnext: "+position );
        Log.w(TAG, "playnext: "+musicList.size() );
      if(musicList.size()-1>position){
          playmusic=musicList.get(position+1);
          }
          else {
          playmusic=musicList.get(0);
      }
      messageEvent.setIsplaying(true);
      messageEvent.setSongname(playmusic.name);
      messageEvent.setSongartist(playmusic.artist);
        EventBus.getDefault().post(messageEvent);
          try {
              mediaPlayer.stop();
              mediaPlayer.reset();
              Isplay=1;
              songUri=Uri.parse(playmusic.songUri);
              Log.w(TAG, "playnext: 音乐属性"+playmusic.name );
              Log.w(TAG, "playnext: 音乐属性"+playmusic.songUri );
              mediaPlayer.setDataSource(getApplicationContext(),songUri);
              mediaPlayer.setOnCompletionListener(onCompletionListener);
              mediaPlayer.prepareAsync();
              mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                  @Override
                  public void onPrepared(MediaPlayer mediaPlayer) {
                      mediaPlayer.seekTo((int) playmusic.playedtime);
                      mediaPlayer.start();
                  }
              });
              Log.w(TAG, "play: 播放成功" );
          }catch (IOException e){
              e.printStackTrace();
              Log.w(TAG, "play: 播放歌曲失败" );
          }
      }

    public void play(){
        if (Isplay==2){//2表示mediaplay是播放后暂停的状态
            mediaPlayer.start();
        }else {
            Isplay=1;
            resetMediaplay();
            if (playmusic==null){
                getMusic();
            }
            messageEvent.setIsplaying(true);
            messageEvent.setSongname(playmusic.name);
            messageEvent.setSongartist(playmusic.artist);
            EventBus.getDefault().post(messageEvent);
            Log.w(TAG, "play: 在服务中的evenyBus事件发布了" );
            try {
                songUri=Uri.parse(playmusic.songUri);
                Log.e(TAG, "play: 要播放的Uri为"+songUri );
                mediaPlayer.setDataSource(getApplicationContext(),songUri);
                mediaPlayer.setOnCompletionListener(onCompletionListener);
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        mediaPlayer.seekTo((int) playmusic.playedtime);
                        mediaPlayer.start();
                    }
                });

                Log.w(TAG, "play: 播放成功" );
            }catch (IOException e){
                e.printStackTrace();
                Log.w(TAG, "play: 播放歌曲失败" );
            }
        }
    }
    public void resetMediaplay(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
    }
    public void pause( ){
        if (mediaPlayer!=null) {
            mediaPlayer.pause();
            Isplay=2;//1表示mediaplay处于暂停状态
            Log.w(TAG, "pause: 停止成功" );
        }else {
            Log.w(TAG, "pause: 歌曲暂停失败" );
        }
    }

    public void playlast(){
        int position=musicList.indexOf(playmusic);
        if (position==0){
            playmusic=musicList.get(musicList.size()-1);}
            else {
            playmusic=musicList.get(position-1);
        }

        messageEvent.setIsplaying(true);
        messageEvent.setSongname(playmusic.name);
        messageEvent.setSongartist(playmusic.artist);
        EventBus.getDefault().post(messageEvent);
            playmusic.playstate=1;
            //myApplication.setMusic(playmusic);
            if(Isplay==2){
                Isplay=1;
            }
            try {
                mediaPlayer.stop();
                mediaPlayer.reset();
                Isplay=1;
                songUri=Uri.parse(playmusic.songUri);
                mediaPlayer.setDataSource(getApplicationContext(),songUri);
                //Log.w(TAG, "loadmusic: "+myApplication.getPlaymusic().songUri );
                mediaPlayer.setOnCompletionListener(onCompletionListener);
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        mediaPlayer.seekTo((int) playmusic.playedtime);
                        mediaPlayer.start();
                    }
                });

                Log.w(TAG, "play: 播放成功" );
            }catch (IOException e){
                e.printStackTrace();
                Log.w(TAG, "play: 播放歌曲失败" );
            }
        }

    public boolean isEmplymusic(){
        if (musicList==null){
            return true;
        }else {
            return false;
        }
    }
    public void setMusicList( String songSheet){
        cruddb=new CRUDdb();
        musicList=cruddb.getmusicList(songSheet);
        Log.w(TAG, "getMusicList: 播放菜单音乐数目"+musicList.size() );

    }
    public void setMusicList(List<Music> musicList){
        this.musicList=musicList;
    }
    public List<Music>getMusicList(){
        return musicList;
    }
    public void getAllmusiclist(){
        CRUDdb cruDdb=new CRUDdb();
        musicList=cruDdb.getAllmusicList();
    }

    public void getMusic(){
        if(playmusic==null){
            playmusic=musicList.get(0);
        }
    }
    public void setMusic(Music music){
        playmusic=music;
    }

}
