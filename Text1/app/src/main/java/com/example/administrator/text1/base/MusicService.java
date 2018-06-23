package com.example.administrator.text1.base;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.administrator.text1.activity.LocalMusic;
import com.example.administrator.text1.bean.Music;
import com.example.administrator.text1.utils.CRUDdb;

import java.io.IOException;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MusicService extends Service {
    List<Music>musicList;
    Music playmusic;
    CRUDdb cruddb=new CRUDdb();
    MyApplication myApplication;
    MediaPlayer mediaPlayer;
    private static int Isplay=0;
    //Isplay=0，是初始状态，=1是播放状态，=2是播放后暂停状态
    public interface MusicListener{
        void start();
        void pause();
        void onplayprogress();
    }
    public MusicService() {
    }
   public MusicService(List<Music>musicList){
        this.musicList=musicList;
        myApplication.setMusicList(musicList);

    }
    public MusicService(List<Music>musiclist,Music playmusic){
        this.musicList=musiclist;
        this.playmusic=playmusic;
        myApplication.setMusicList(musicList);
        myApplication.setMusic(playmusic);
    }
    @Override
    public void onCreate() {
        super.onCreate();
        Log.w("MusicService", "onCreate: MusicService成功" );

       NotificationCompat.Builder builder=new NotificationCompat.Builder(getBaseContext());
        Intent intent=new Intent(this,LocalMusic.class);
        PendingIntent pi=PendingIntent.getActivity(this,0,intent,0);
        Notification notification=new NotificationCompat.Builder(this,"channerId")
                .setContentTitle("这是前台服务的头")
                .setContentText("这是前台服务的内容")
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pi)
                .build();
        startForeground(1,notification);
        Log.w(TAG, "onCreate: 前台服务已开启" );
        myApplication=(MyApplication)getApplication();
        if (myApplication.getPlaymusic()!=null){
            playmusic=myApplication.getPlaymusic();
        }
        Log.w(TAG, "onCreate: 获得了myApplication实例" );
        mediaPlayer=new MediaPlayer();
        mediaPlayer.setOnCompletionListener(onCompletionListener);

        Log.w(TAG, "onCreate: 服务启动成功" );
        musicList=myApplication.getMusicList();

    }
    private MediaPlayer.OnCompletionListener onCompletionListener=new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            playmusic.playedtime=0;
            cruddb.update(playmusic);

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
    private final IBinder musicBinder=new MusicBinder();
    @Override
    public IBinder onBind(Intent intent) {
        return musicBinder;
    }
    public class MusicBinder extends Binder{
       public MusicService getService(){
            return MusicService.this;
        }
    }
   public void playnext(){
      int position=musicList.indexOf(playmusic);
      if(musicList.size()-1>position){
          playmusic=musicList.get(position+1);
          myApplication.setMusic(playmusic);
          if(Isplay==1){
              mediaPlayer.stop();
              mediaPlayer.reset();
          }
          if(Isplay==2){
              mediaPlayer.stop();
              mediaPlayer.reset();
              Isplay=1;
          }

          try {
              Isplay=1;
              mediaPlayer.setDataSource(myApplication.getContext(),playmusic.songUri);
              Log.w(TAG, "loadmusic: "+myApplication.getPlaymusic().songUri );
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

      }else if (musicList.size()-1==position){
          playmusic=musicList.get(0);
          myApplication.setMusic(playmusic);
          if(Isplay==2){
              mediaPlayer.stop();
              mediaPlayer.reset();
              Isplay=1;
          }
          try {
              Isplay=1;
              mediaPlayer.setDataSource(myApplication.getContext(),playmusic.songUri);
              Log.w(TAG, "loadmusic: "+myApplication.getPlaymusic().songUri );
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
   public void play(){
        if (Isplay==2){//2表示mediaplay是播放后暂停的状态
            mediaPlayer.start();
        }else {
            Isplay=1;
            if(mediaPlayer.isPlaying()){
                mediaPlayer.stop();
                mediaPlayer.reset();
            }
        playmusic=myApplication.getPlaymusic();
        Log.w(TAG, "play:要播放的音乐的名字是"+playmusic.name);
        if (playmusic!=null){
            try {
                mediaPlayer.setDataSource(myApplication.getContext(),playmusic.songUri);
                Log.w(TAG, "loadmusic: "+myApplication.getPlaymusic().songUri );
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
    void playlast(){
        int position=musicList.indexOf(playmusic);
        if (position==0){
            playmusic=musicList.get(musicList.size()-1);
            myApplication.setMusic(playmusic);
            if (Isplay==1){
                mediaPlayer.stop();
                mediaPlayer.reset();
            }
            if(Isplay==2){
                mediaPlayer.stop();
                mediaPlayer.reset();
                Isplay=1;
            }
            try {
                Isplay=1;
                mediaPlayer.setDataSource(myApplication.getContext(),playmusic.songUri);
                Log.w(TAG, "loadmusic: "+myApplication.getPlaymusic().songUri );
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
        }else {
            playmusic=musicList.get(position-1);
            myApplication.setMusic(playmusic);
            if(Isplay==2){
                mediaPlayer.stop();
                mediaPlayer.reset();
                Isplay=1;
            }
            try {
                Isplay=1;
                mediaPlayer.setDataSource(myApplication.getContext(),playmusic.songUri);
                Log.w(TAG, "loadmusic: "+myApplication.getPlaymusic().songUri );
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
}
