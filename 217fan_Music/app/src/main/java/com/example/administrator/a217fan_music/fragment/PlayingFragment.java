package com.example.administrator.a217fan_music.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.example.administrator.a217fan_music.R;
import com.example.administrator.a217fan_music.utils.CRUDdb;
import com.example.administrator.a217fan_music.utils.MusicService;


public class PlayingFragment extends Fragment implements View.OnClickListener{

    private SeekBar seekBar;
    private Intent progress_change_to_service;
    private Intent start_service;
    private static int isPlayOrPause=1;//如果等于1是暂停状态，等于2是播放状态
    ImageView playing_random;
    ImageView playing_press;
    ImageView playing_play;
    ImageView playing_next;
    ImageView playing_playlist;
    CRUDdb cruDdb;
    public PlayingFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_playing, container,false);
                start_service=new Intent(getActivity(), MusicService.class);
        getActivity().bindService(start_service,mServiceConnection, Context.BIND_AUTO_CREATE);
        playing_play=(ImageView)view.findViewById(R.id.playing_play);
        playing_press=(ImageView)view.findViewById(R.id.playing_press);
        playing_next=(ImageView)view.findViewById(R.id.playing_next);
        playing_next.setOnClickListener(this);
        playing_play.setOnClickListener(this);
        playing_press.setOnClickListener(this);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
    MusicService mMusicService;
    ServiceConnection mServiceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder=(MusicService.MusicBinder)service;
            mMusicService=binder.getService();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.playing_play:
               if (isPlayOrPause==1){
                   playing_play.setImageResource(R.drawable.landscape_player_btn_pause_normal);
                   mMusicService.setMusicList("本地音乐");
                   mMusicService.getMusic();
                   mMusicService.play();
                   isPlayOrPause=2;
               }else {
                   playing_play.setImageResource(R.drawable.landscape_player_btn_play_normal);
                   mMusicService.pause();
                   isPlayOrPause=1;
               }
                break;
            case R.id.playing_next:
                mMusicService.playnext();
                break;
            case R.id.playing_press:
                mMusicService.playlast();
        }

    }
}
