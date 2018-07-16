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
import com.example.administrator.a217fan_music.utils.Http;
import com.example.administrator.a217fan_music.utils.MusicService;

import java.util.List;


public class LineMusic_State_3 extends Fragment {

    static String TAG="Line_State_3";
    MusicService musicService;
    ListView listView;
    MusicAdapter musicAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_line_music__state_3, container, false);
        Log.w(TAG, "onCreateView: 执行了" );
        Intent intent=new Intent(getActivity(),MusicService.class);
        getActivity().bindService(intent,mserviceConnection,Context.BIND_AUTO_CREATE);
        listView=(ListView)view.findViewById(R.id.line_Music_state3_listView);
        musicAdapter=new MusicAdapter(getActivity(),R.layout.music_item, Http.list);
        listView.setAdapter(musicAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.w(TAG, "onResume: 执行了" );
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                musicAdapter.notifyDataSetChanged();
                Music music=Http.list.get(position);
                List<Music>list=musicService.getMusicList();
                list.add(music);
                musicService.setMusicList(list);
                musicService.setMusic(music);
                //musicService.resetMediaplay();
                //musicService.play();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.w(TAG, "onPause: 执行了" );
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.w(TAG, "onStop: 执行了" );
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.w(TAG, "onAttach: 执行了" );
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.w(TAG, "onDetach: 执行了" );
    }

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
}
