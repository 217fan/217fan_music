package com.example.administrator.a217fan_music.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.a217fan_music.R;


public class MainLocalMusic extends Fragment implements View.OnClickListener{
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    LocalMusicContent localMusicContent;
    ImageView localmusic;
    ImageView dowmmusic;
    ImageView isfavorite;
    TextView activitySearch;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         fragmentManager=getFragmentManager();
         fragmentTransaction=fragmentManager.beginTransaction();
         localMusicContent=new LocalMusicContent();
         View view=inflater.inflate(R.layout.fragment_local_music_main, container, false);
         localmusic=(ImageView)view.findViewById(R.id.activity_main_localmusic_picture);
         localmusic.setOnClickListener(this);
         dowmmusic=(ImageView)view.findViewById(R.id.activity_main_downmusic_picture);
         isfavorite=(ImageView)view.findViewById(R.id.activity_main_isfavorite_picture);
         dowmmusic.setOnClickListener(this);
         isfavorite.setOnClickListener(this);
         activitySearch=(TextView)getActivity().findViewById(R.id.activity_main_search);
         activitySearch.setVisibility(View.VISIBLE);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_main_localmusic_picture:

                    fragmentTransaction.replace(R.id.activity_main_fragment,localMusicContent).addToBackStack(null).commit();

                    Log.w("本地按钮", "onClick: " );
                    break;

        }
    }
}
