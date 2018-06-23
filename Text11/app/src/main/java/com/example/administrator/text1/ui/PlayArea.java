package com.example.administrator.text1.ui;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.administrator.text1.R;
import com.example.administrator.text1.base.MusicService;
import com.example.administrator.text1.base.MyApplication;

public class PlayArea extends LinearLayout {


    public boolean isplay;
    public boolean isshowplaylist;
    public boolean isshowplayfg;
    public ImageView albumpic;
    public LinearLayout songdata;
    public ImageView play;
    public ImageView playlist;
    public TextView songname;
    public TextView songartist;
    Context mcontext;
    public PlayArea(Context context, AttributeSet attributes) {
        super(context,attributes);

        load(context);
    }
    public PlayArea(Context context){
        super(context);
        load(context);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {

        super.setOnClickListener(l);
    }

    private void load(Context context){
        mcontext=context;
        isplay=false;
        isshowplayfg=false;
        isshowplaylist=false;
        LayoutInflater.from(context).inflate(R.layout.play_area,this);
        albumpic=(ImageView)findViewById(R.id.albumpic);
       // albumpic.setOnClickListener(this);

        songdata=(LinearLayout)findViewById(R.id.song_data);
       // songdata.setOnClickListener(this);
        play=(ImageView)findViewById(R.id.play);
       // play.setOnClickListener(this);
        playlist=(ImageView)findViewById(R.id.playlist);
       // playlist.setOnClickListener(this);
        songname=(TextView)findViewById(R.id.song_name);
        songartist=(TextView)findViewById(R.id.song_artist);
    }

  /*  @Override
    public void onClick(View view) {
       switch (view.getId()){
            case R.id.albumpic:
                isshowplayfg=true;
                Log.w("playarea onclick","ishhowolayfg==true" );
                break;
            case R.id.song_data:
                isshowplayfg=true;
                Log.w("playarea onclick","ishhowolayfg==true" );
                break;
            case R.id.play:
                Log.w("playarea", "onClick: play执行了");
                if (isplay == false) {
                    play.setImageResource(R.drawable.landscape_player_btn_pause_normal);
                    Log.w("playarea onclick","ispaly=true" );
                    isplay = true;
                } else {
                    play.setImageResource(R.drawable.landscape_player_btn_play_normal);
                    Log.w("playarea onclick","ispaly=false" );
                    isplay = false;
                }
                break;
            case R.id.playlist:
                if (isshowplaylist==false){
                    isshowplaylist=true;
                    Log.w("playarea onclick","isshowplaylist=true" );
                }else {
                    isshowplaylist=false;
                    Log.w("playarea onclick","isshowplaylist=false" );
                }
                break;
                default:
                    break;
        }
    }*/
    /* public boolean isplay;
    public boolean isshowplaylist;
    public boolean isshowplayfg;
    ImageView albumpic;
    LinearLayout songdata;
    ImageView play;
    ImageView playlist;
    int id;
    Context mcontext;
    public PlayArea(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.play_area,this);
         albumpic=(ImageView)findViewById(R.id.albumpic);
        songdata=(LinearLayout)findViewById(R.id.song_data);
         play=(ImageView)findViewById(R.id.play);
         playlist=(ImageView)findViewById(R.id.playlist);
        Intent intent=new Intent(context,MusicService.class);
        mcontext=context;
        isplay=false;
        isshowplayfg=false;
        isshowplaylist=false;
    }
    @Override
    public void onClick(View view) {
        switch ((view.getId())){
            case R.id.play:
                if (isplay==false){
                    play.setImageResource(R.drawable.landscape_player_btn_pause_normal);
                    isplay=true;
                }else {
                    play.setImageResource(R.drawable.landscape_player_btn_play_normal);
                    isplay=false;
                }
                break;
            case R.id.playlist:{
                if (isshowplaylist==false){
                    isshowplaylist=true;
                }else {
                    isshowplaylist=false;
                }
            }



        }
    }*/

}
