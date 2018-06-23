package com.example.administrator.text1.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import com.example.administrator.text1.R;
import com.example.administrator.text1.adapter.MusicAdapter;
import com.example.administrator.text1.base.MusicService;
import com.example.administrator.text1.base.MyApplication;
import com.example.administrator.text1.bean.Music;
import com.example.administrator.text1.ui.PlayArea;
import com.example.administrator.text1.fragment.playlist;

import java.util.ArrayList;
import java.util.List;

public class LocalMusic extends AppCompatActivity {


   MusicAdapter musicAdapter;
    List<Music> list = new ArrayList<>();
    MyApplication myApplication=(MyApplication)getApplication();
    private static String  TAG = "LocalMusic";
    PlayArea playArea;
    LayoutInflater layoutInflater;
    playlist playlist1;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_local);

        if (ContextCompat.checkSelfPermission(LocalMusic.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LocalMusic.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else {
            loadmusic();
        }
        Intent startservice=new Intent(this,MusicService.class);
        startService(startservice);
        bindService(startservice,mserviceConnection,BIND_AUTO_CREATE);
       //执行playArea的监听事件
      playArea=(PlayArea)findViewById(R.id.playarea);
     playArea.play.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {

              if (playArea.isplay == false){
                  playArea.play.setImageResource(R.drawable.landscape_player_btn_pause_normal);
                  playArea.isplay=true;
                  Log.w("playarea onclick","ispaly=true" );
                 musicservice.play();
              } else {
                 playArea.play.setImageResource(R.drawable.landscape_player_btn_play_normal);
                 playArea.isplay=false;
                  Log.w("playarea onclick","ispaly=false" );
                musicservice.pause();
              }
          }
      });
        playArea.playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               /*playlist1=new playlist();
               fragmentTransaction=fragmentManager.beginTransaction();
               fragmentTransaction.add(R.id.playarea,playlist1);
               fragment
               Transaction.commit();*/
               Intent intent=new Intent(LocalMusic.this,SearchMusic.class);
               startActivity(intent);
            }
        });
        musicAdapter=new MusicAdapter(LocalMusic.this,R.layout.musicitem,list);
        ListView musiclist=(ListView)findViewById(R.id.musiclist);
        musiclist.setAdapter(musicAdapter);
        musiclist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Music music=list.get(i);
                myApplication.setMusic(music);
                playArea.songname.setText(music.name);
                playArea.songartist.setText(music.artist);
               // playArea.albumpic.setImageURI(  music.albumUri);
                playArea.play.setImageResource(R.drawable.landscape_player_btn_pause_normal);
                playArea.isplay=true;
                musicservice.play();

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent stopservice=new Intent(this,MusicService.class);
        stopService(stopservice);
        unbindService(mserviceConnection);
    }

    private void loadmusic() {
        Uri uri=MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if (cursor!=null) {
            while (cursor.moveToNext()){
                String id=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                Uri songUri=Uri.withAppendedPath(uri,id);
                String name=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                String artist=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                int albumid=cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
                String albumname=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
                Uri albumUri= ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"),albumid);
                Long duration=cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                Music music=new Music(name,artist,songUri,albumUri,duration,0,false);
                music.albumname=albumname;

                list.add(music);
            }
        }
        cursor.close();
        myApplication=(MyApplication)getApplication();
        Log.w(TAG, "loadmusic: 得到myapplication的实例" );
       myApplication.setMusicList(list);
       if (list.size()>0){
           myApplication.setMusic(list.get(0));
       }
        Log.w(TAG, "loadmusic: "+myApplication.getPlaymusic().songUri );
        Log.w(TAG, "loadmusic: 歌曲名字是"+myApplication.getPlaymusic().name );
        Log.w(TAG, "loadmusic: 搜索本地音乐完成并且设置全局变量musiclist.music成功");
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    loadmusic();
                }else {
                        Toast.makeText(LocalMusic.this,"拒绝将无法使用权限",Toast.LENGTH_SHORT).show();
                        finish();

                }
                break;
                default:
        }
    }

    MusicService musicservice;
    ServiceConnection  mserviceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicService.MusicBinder binder=(MusicService.MusicBinder)iBinder;
            musicservice=binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };
}