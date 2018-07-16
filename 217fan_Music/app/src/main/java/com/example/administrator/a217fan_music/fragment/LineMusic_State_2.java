package com.example.administrator.a217fan_music.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.administrator.a217fan_music.R;
import com.example.administrator.a217fan_music.adapter.MusicAdapter;
import com.example.administrator.a217fan_music.bean.Music;
import com.example.administrator.a217fan_music.utils.Http;
import com.example.administrator.a217fan_music.utils.MessageWord;
import com.example.administrator.a217fan_music.utils.MusicService;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class LineMusic_State_2 extends Fragment {

    ListView searchListView;
    List<String>musicList;
    ArrayAdapter adapter;

    MusicService musicService;
    Http http;
    FragmentManager fm;
    LineMusic_State_3 lineMusic_state_3;
    static String word;
    final static String TAG="Line_State2";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=new Intent(getActivity(),MusicService.class);
        getActivity().bindService(intent,mserviceConnection,Context.BIND_AUTO_CREATE);
        lineMusic_state_3=new LineMusic_State_3();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.w(TAG, "onCreateView: 执行了");
            View view=inflater.inflate(R.layout.fragment_line_music_state_2, container, false);
            searchListView=(ListView)view.findViewById(R.id.line_music_state2_listView);
            musicList=new ArrayList<>();
            adapter=new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,musicList);


        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.w(TAG, "onResume: 执行了");
        for (int i=0;i<Http.list.size();i++){

            musicList.add(Http.list.get(i).name);
        }

        searchListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               word=musicList.get(position);
                MessageWord messageWord=new MessageWord();
                messageWord.setWord(word);
                EventBus.getDefault().post(messageWord);
               getid();
               while (Http.isFinishid==false){

               }
               getSongUrl();
               fm=getFragmentManager();

               fm.beginTransaction().replace(R.id.line_Main_Fragment,lineMusic_state_3).commit();

            }

        });
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.w(TAG, "onPause: 执行了");
    }

    @Override
    public void onDetach() {
        Log.w(TAG, "onDetach: 执行了");
        super.onDetach();
        http=null;
    }
    void getid(){
        Http.list.clear();
        http=new Http();
        http.getid_Json(word, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.w(TAG, "onFailure: 失败了" );
                Log.w(TAG, "onFailure: "+e.toString() );
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //Log.w(TAG, "onResponse: "+response.body().string() );
                parsegetJsonid(response.body().string());
                //  Log.w(TAG, "onResponse: "+Http.list.size());
                Log.w(TAG, "onResponse: " +Http.list.size() );
                Log.w(TAG, "onResponse: 解析id成功" );
                Http.isFinishid=true;
            }
        });
    }
    private void parsegetJsonid(String response){
        try{
            JSONObject jsonObject=new JSONObject(response);
            JSONObject jsonObject1=jsonObject.getJSONObject("result");
            JSONArray jsonArray=jsonObject1.getJSONArray("songs");

            Log.w(TAG, "parsegetJsonid: songs"+jsonArray );
            // JSONObject jsonObjectalbum=jsonObject1.getJSONObject("album");
            //Log.w(TAG, "parsegetJsonid:album "+jsonObjectalbum );
            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject2=(JSONObject)jsonArray.get(i);
                JSONArray jsonArrayartist=jsonObject2.getJSONArray("artists");
                Log.w(TAG, "parsegetJsonid: JsonArrayartist.length"+jsonArrayartist.length() );
                JSONObject jsonObjectalbum=jsonObject2.getJSONObject("album");
                JSONObject jsonObjectartist=(JSONObject)jsonArrayartist.get(0);
                Music music=new Music();
                music.name=jsonObject2.getString("name");
                music.line_songId=jsonObject2.getString("id");
                music.artist=jsonObjectartist.getString("name");
                music.albumname=jsonObjectalbum.getString("name");
                Log.w(TAG, "parsegetJsonid:id "+music.line_songId);
                Log.w(TAG, "parsegetJsonid:artist "+music.artist );
                Log.w(TAG, "parsegetJsonid: albumname"+music.albumname );
                Http.list.add(music);
            }

        }catch (Exception e){
            e.printStackTrace();
            Log.w(TAG, "parseJson: 失败"+e.toString() );
        }
    }
    void getSongUrl(){
        Http http=new Http();
        Log.w(TAG, "getSongUrl: "+Http.list.size() );
        if (Http.list!=null){
            for (int i=0;i<Http.list.size();i++){
                Http.music=Http.list.get(i);
                http.getSongUrl_Json(Http.music.line_songId, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.w(TAG, "onFailure: 失败了" );
                        Log.w(TAG, "onFailure: "+e.toString() );
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        int i=Http.list.indexOf(Http.music);
                        String re=response.body().string();
                        re=re.replaceAll("\\\\","");
                        Http.music.Url= parsegetJsonsongUrl(re);
                        //  Log.w(TAG, "onResponse: "+re);
                        Http.list.set(i,Http.music);
                        Http.isIsFinishUrl=true;
                    }
                });
                int i1=0;
                while (Http.isIsFinishUrl!=true){
                    i1++;
                }
            }}
    }

    private String parsegetJsonsongUrl(String response){
        try {
            JSONObject jsonObject=new JSONObject(response);
            JSONArray jsonArray=jsonObject.getJSONArray("data");
            JSONObject jsonObject1=jsonArray.getJSONObject(0);
            Log.w(TAG, "parsegetJsonsongUrl: "+ jsonObject1);
            String url=jsonObject1.getString("url");
            return url;
        }catch (Exception e){
            e.printStackTrace();
            Log.w(TAG, "parsegetJsonsongUrl: 解析失败" );
            Log.w(TAG, "parsegetJsonsongUrl: "+e.toString() );
            return null;
        }

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
