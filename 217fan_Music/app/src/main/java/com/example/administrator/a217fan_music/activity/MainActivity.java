package com.example.administrator.a217fan_music.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.a217fan_music.R;
import com.example.administrator.a217fan_music.adapter.PresentMusicItemAdapter;
import com.example.administrator.a217fan_music.bean.Music;
import com.example.administrator.a217fan_music.fragment.LineMusic_Main;
import com.example.administrator.a217fan_music.fragment.LineMusic_State_1;
import com.example.administrator.a217fan_music.fragment.LocalMusicContent;
import com.example.administrator.a217fan_music.fragment.MainLocalMusic;
import com.example.administrator.a217fan_music.fragment.PlayingFragment;
import com.example.administrator.a217fan_music.utils.CRUDdb;
import com.example.administrator.a217fan_music.utils.MessageEvent;
import com.example.administrator.a217fan_music.utils.MusicService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG ="MainActivity" ;
    private static boolean isplaying=false;
    android.support.v4.app.FragmentManager fragmentManager;
    LineMusic_Main lineMusic_main;
    MainLocalMusic mainLocalMusic;
    List<Music> list;
    CRUDdb cruddb;
    TextView activitySerch;
    ImageView playArea_play;
    ImageView playArea_playlist;
    TextView playArea_songname;
    TextView playArea_songartist;
    TextView playingSongname;
    ImageView playingPlay;
    LinearLayout playArea_songdata;
    PopupWindow popupWindow;
    PopupWindow PlayingpopupWindow;
    MessageEvent messageEvent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
        Log.w(TAG, "onCreate: oCreate执行了" );
      setContentView(R.layout.activity_main);
      activitySerch=(TextView) findViewById(R.id.activity_main_search);
      activitySerch.setVisibility(View.VISIBLE);
      playArea_play=(ImageView)findViewById(R.id.playArea_play);
      playArea_playlist=(ImageView)findViewById(R.id.playArea_playlist);
      playArea_songname=(TextView)findViewById(R.id.activity_playArea_song_name);
      playArea_songartist=(TextView)findViewById(R.id.activity_playArea_song_artist);
      playArea_songdata=(LinearLayout)findViewById(R.id.activity_playArea_song_data);
        //申请权限，将数据载入数据库
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else {
            loaddata();
        }
        //载入MainLocalMusic
        fragmentManager=getSupportFragmentManager();
        mainLocalMusic=new MainLocalMusic();
        fragmentManager.beginTransaction().add(R.id.activity_main_fragment,mainLocalMusic).show(mainLocalMusic).commit();
        //启动绑定服务
        Intent intent=new Intent(MainActivity.this,MusicService.class);
        startService(intent);
        bindService(intent,mserviceConnection,BIND_AUTO_CREATE);
        //使用EventBus进行通信
        //注册成为订阅者
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        Log.w(TAG, "onResume: onResume执行了" );
        super.onResume();
        lineMusic_main=new LineMusic_Main();

        activitySerch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              fragmentManager.beginTransaction().replace(R.id.activity_main_fragment,lineMusic_main).addToBackStack(null).commit();
              activitySerch.setVisibility(View.GONE);
            }
        });
        playArea_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isplaying){
                    isplaying=false;
                    playArea_play.setImageResource(R.drawable.landscape_player_btn_play_normal);
                    musicService.pause();
                }else {
                    isplaying=true;
                    playArea_play.setImageResource(R.drawable.landscape_player_btn_pause_normal);
                    musicService.play();
                }
            }
        });
        playArea_playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindowPlaylist();
            }
        });
        playArea_songname.setOnClickListener(this);
        playArea_songartist.setOnClickListener(this);
        playArea_songdata.setOnClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w(TAG, "onPause: onPuse执行了" );

    }

    @Override
    protected void onStop() {
        Log.w(TAG, "onStop: onstop执行了" );
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.w(TAG, "onDestroy: destory执行了" );
        Log.w(TAG, "onPause: 订阅事件注销了" );
        EventBus.getDefault().unregister(this);
        Intent stopservice=new Intent(MainActivity.this,MusicService.class);
        stopService(stopservice);
    }
    void loaddata(){
        list=new ArrayList<>();
        Uri uri= MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
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
                Music music=new Music();
                String SsongUri=songUri.toString();
                music.songUri=SsongUri;
                music.playstate=0;
                music.state="本地";
                music.name=name;
                music.Url=null;
                music.artist=artist;
                music.albumname=albumname;
                music.duration=duration;
                String SalbumUri=albumUri.toString();
                music.albumUri=SalbumUri;
                Log.w(TAG, "loadmusic:songUri "+music.songUri );
                Log.w(TAG, "loadmusic: duration"+music.duration );
                music.setSongSheet("本地音乐");
                list.add(music);
            }
        }
        cursor.close();
            cruddb = new CRUDdb();
            cruddb.creatda();
            Music.deleteAll(Music.class);
            Log.w(TAG, "loaddata:数据库数目 "+list.size() );
            cruddb.incread(list);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    loaddata();
                }else {
                    Toast.makeText(MainActivity.this,"拒绝将无法使用权限",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }

    }
    MusicService musicService;
    ServiceConnection mserviceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder=(MusicService.MusicBinder)service;
            musicService=binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private void  showPopupWindowPlaylist(){
        LayoutInflater layoutInflater=(LayoutInflater)getSystemService(MainActivity.LAYOUT_INFLATER_SERVICE);
        View contentView =layoutInflater.inflate(R.layout.popupwindow_playlist,null);
        popupWindow =new PopupWindow(contentView);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        ListView listView=(ListView)contentView.findViewById(R.id.presentplaylist);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Music music=new Music();
                music=list.get(position);
                MessageEvent messageEvent=new MessageEvent();
                messageEvent.setSongartist(music.artist);
                messageEvent.setSongname(music.name);
                messageEvent.setIsplaying(true);
                EventBus.getDefault().post(messageEvent);
                musicService.resetMediaplay();
                musicService.setMusic(music);
                musicService.play();
            }
        });
        list=musicService.getMusicList();
      final PresentMusicItemAdapter presentMusicItemAdapter=new PresentMusicItemAdapter(getApplicationContext(),R.layout.present_music_item,list);
        presentMusicItemAdapter.setonItemDelectListener(new PresentMusicItemAdapter.onItemDelectListener() {
            @Override
            public void onDelectonＣlick(int i) {
                list.remove(i);
               // musicService.setMusicList(list);
                presentMusicItemAdapter.notifyDataSetChanged();
            }
        });
        listView.setAdapter(presentMusicItemAdapter);
        TextView closeplaylist=(TextView)contentView.findViewById(R.id.closeplaylist);
        closeplaylist.setOnClickListener(this);
        ImageView clearAllparsentMusic=(ImageView)contentView.findViewById(R.id.delect_music);
        clearAllparsentMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("提示");
                dialog.setMessage("确定清空所有音乐");
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        list.clear();
                        musicService.setMusicList(list);
                        presentMusicItemAdapter.notifyDataSetChanged();
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();

            }
        });
        //显示PopupWindow
        View rootview=LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main,null);
        popupWindow.showAtLocation(rootview, Gravity.BOTTOM, 0,0);
    }
    private void showPlayingPopupWindow(){
        LayoutInflater layoutInflater=(LayoutInflater)getSystemService(MainActivity.LAYOUT_INFLATER_SERVICE);
        View contentView =layoutInflater.inflate(R.layout.fragment_playing,null);
        PlayingpopupWindow =new PopupWindow(contentView);
        PlayingpopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        PlayingpopupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        PlayingpopupWindow.setFocusable(true);
        PlayingpopupWindow.setBackgroundDrawable(new BitmapDrawable());
        playingSongname=(TextView)contentView.findViewById(R.id.playing_songName);
        playingPlay=(ImageView)contentView.findViewById(R.id.playing_play);
        ImageView playingPlaynext=(ImageView)contentView.findViewById(R.id.playing_next);
        ImageView playingPlaylast=(ImageView)contentView.findViewById(R.id.playing_press);
        ImageView playingPlaylist=(ImageView)contentView.findViewById(R.id.playing_playlist);
        TextView  playingPlayedtimeK=(TextView)contentView.findViewById(R.id.playing_playedtime_K);
        TextView  playingPlayedtimeJ=(TextView)contentView.findViewById(R.id.playing_playedtime_J);
        SeekBar seekBar=(SeekBar)contentView.findViewById(R.id.playing_SeekBar);

        playingPlaylist.setOnClickListener(this);
        playingPlaylast.setOnClickListener(this);
        playingPlay.setOnClickListener(this);
        playingPlaylast.setOnClickListener(this);
        playingPlaynext.setOnClickListener(this);
        PlayingpopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
           // EventBus.getDefault().unregister(this);
            }
        });
        //显示PopupWindow
        View rootview=LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main,null);
        PlayingpopupWindow.showAtLocation(rootview, Gravity.BOTTOM, 0,0);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.closeplaylist:
                popupWindow.dismiss();
                break;
            case R.id.activity_playArea_song_data:
                Log.w(TAG, "onClick: 展现playing界面" );
                showPlayingPopupWindow();
               // EventBus.getDefault().register(this);
                messageEvent=new MessageEvent();
                messageEvent.setIsplaying(isplaying);
                messageEvent.setSongname(playArea_songname.getText().toString());
                messageEvent.setSongartist(playArea_songartist.getText().toString());
               EventBus.getDefault().post(messageEvent);
                break;
            case R.id.playing_playlist:
                showPopupWindowPlaylist();
                messageEvent=new MessageEvent();

                EventBus.getDefault().post(messageEvent);
                Log.w(TAG, "onClick: 展现playing_playlist" );

                break;
            case R.id.playing_next:
                Log.w(TAG, "onClick: 展现playing_next" );
                musicService.playnext();
                break;
            case R.id.playing_press:
                Log.w(TAG, "onClick: 展现playing_last" );
                musicService.playlast();
                break;
            case R.id.playing_play:
                Log.w(TAG, "onClick: 展现playing_play" );
                if (isplaying){
                    isplaying=false;
                    playArea_play.setImageResource(R.drawable.landscape_player_btn_play_normal);
                    playingPlay.setImageResource(R.drawable.landscape_player_btn_play_normal);
                    musicService.pause();
                }else {
                    isplaying=true;
                    playArea_play.setImageResource(R.drawable.landscape_player_btn_pause_normal);
                    playingPlay.setImageResource(R.drawable.landscape_player_btn_pause_normal);
                    musicService.play();
                }
                break;

            case R.id.activity_main_search:

        }

    }
    //订阅方法，当接受到事件的时候会调用该方法
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent messageEvent){
        isplaying=messageEvent.getIsplaying();
        if(messageEvent.getIsplaying()){
            playArea_play.setImageResource(R.drawable.landscape_player_btn_pause_normal);
        }else {
            playArea_play.setImageResource(R.drawable.landscape_player_btn_play_normal);
        }
        playArea_songname.setText(messageEvent.getSongname());
        playArea_songartist.setText(messageEvent.getSongartist());
        if (PlayingpopupWindow.isShowing()){
            Log.w(TAG, "onEvent: PlayingPopWindow出现" );
            playingSongname.setText(messageEvent.getSongname());
            if(messageEvent.getIsplaying()){
                playingPlay.setImageResource(R.drawable.landscape_player_btn_pause_normal);
            }else {
               playingPlay.setImageResource(R.drawable.landscape_player_btn_play_normal);
            }
        }
        Log.w(TAG, "onEvent: 事件执行成功" );
    }
}
