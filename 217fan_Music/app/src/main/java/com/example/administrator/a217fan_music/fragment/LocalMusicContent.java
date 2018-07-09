package com.example.administrator.a217fan_music.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.administrator.a217fan_music.R;
import com.example.administrator.a217fan_music.adapter.MusicAdapter;
import com.example.administrator.a217fan_music.bean.Music;
import com.example.administrator.a217fan_music.utils.CRUDdb;
import com.example.administrator.a217fan_music.utils.MessageEvent;
import com.example.administrator.a217fan_music.utils.MusicService;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


public class LocalMusicContent extends Fragment {
    CRUDdb cruddb;
    List<Music> musicList;
    ListView listView;
    Music music;
    MessageEvent messageEvent;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       View view=inflater.inflate(R.layout.fragment_local_music_content, container, false);
       cruddb=new CRUDdb();
       musicList=new ArrayList<>();
       musicList=cruddb.getmusicList("本地音乐");
        final MusicAdapter musicAdapter=new MusicAdapter(getActivity(),R.layout.music_item,musicList);
       listView=(ListView)view.findViewById(R.id.local_music_content_listview);
       listView.setAdapter(musicAdapter);
       //声明MessageEvent
        messageEvent=new MessageEvent();
        //绑定服务
        Intent startservice=new Intent(getActivity(),MusicService.class);
        getActivity().startService(startservice);
        getActivity().bindService(startservice,mserviceConnection,Context.BIND_AUTO_CREATE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                music=musicList.get(position);
                if (music==null){
                    Log.w("", "onItemClick: 音乐为空" );
                }
                //为messageevent设置值并发送事件
                messageEvent.setIsplaying(true);
                messageEvent.setSongname(music.name);
                messageEvent.setSongartist(music.artist);
                EventBus.getDefault().post(messageEvent);
                Log.w(TAG, "onItemClick: eventbus事件发送成功" );
                //服务方法
                musicService.resetMediaplay();
                musicService.setMusicList(musicList);
                musicService.setMusic(music);
                musicService.play();
            }
        });
       return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
    MusicService musicService;
    ServiceConnection mserviceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicService.MusicBinder binder=(MusicService.MusicBinder)iBinder;
           musicService=binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };
}
