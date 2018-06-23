package com.example.administrator.text1.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.administrator.text1.R;
import com.example.administrator.text1.adapter.MusicAdapter;
import com.example.administrator.text1.bean.Music;
import com.example.administrator.text1.utils.Http;

import java.util.List;


public class SearchFragment3 extends Fragment {
    ListView listView;
    MusicAdapter musicAdapter;
    List<Music>musicList;
    static String TAG="SearchFragment3";
    public SearchFragment3() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         View view=inflater.inflate(R.layout.fragment_search_fragment3, container, false);
        listView=(ListView)view.findViewById(R.id.list_music);
        musicAdapter=new MusicAdapter(getActivity(),R.layout.musicitem, Http.list);
        listView.setAdapter(musicAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Music music=Http.music;
                Log.w(TAG, "onItemClick: 点击成功");
            }
        });
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

}
