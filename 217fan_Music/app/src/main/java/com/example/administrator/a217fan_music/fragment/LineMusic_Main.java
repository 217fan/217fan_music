package com.example.administrator.a217fan_music.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.administrator.a217fan_music.R;
import com.example.administrator.a217fan_music.bean.Music;
import com.example.administrator.a217fan_music.utils.Http;
import com.example.administrator.a217fan_music.utils.MessageWord;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LineMusic_Main extends android.support.v4.app.Fragment {
    private final static String TAG="LinMusic_Main";
    EditText searchEdit;

    LineMusic_State_1 lineMusic_state_1;
    LineMusic_State_2 lineMusic_state_2;
    LineMusic_State_3 lineMusic_state_3;
    static String word;
    static int  SEARCH=1;
    Http http;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_line_main, container, false);
        searchEdit=(EditText)view.findViewById(R.id.line_Search);
        searchEdit.setFocusable(true);

        lineMusic_state_1=new LineMusic_State_1();
        lineMusic_state_2=new LineMusic_State_2();
        lineMusic_state_3=new LineMusic_State_3();
        FragmentManager fm=getChildFragmentManager();
        fm.beginTransaction().add(R.id.line_Main_Fragment,lineMusic_state_1).show(lineMusic_state_1).commit();
        EventBus.getDefault().register(this);
        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (handler.hasMessages(SEARCH)){
                    handler.removeMessages(SEARCH);
                }
                handler.sendEmptyMessageDelayed(SEARCH,400);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.w(TAG, "onResume: 执行了" );


    }

    @Override
    public void onPause() {
        super.onPause();
        Log.w(TAG, "onPause: 执行了" );
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        Log.w(TAG, "onStop: 执行了" );
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Log.w(TAG, "onAttach: 执行了" );
    }

    @Override
    public void onDetach() {
        Log.w(TAG, "onDetach: 执行了" );
        super.onDetach();
        http=null;
        SEARCH=2;
        searchEdit.setText(null);

    }

     private  Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==SEARCH){
                if (searchEdit.getText()!=null){
                    word=searchEdit.getText().toString();
                    getid();
                    while (Http.isFinishid==false) {
                        int i = 1;
                    }
                    FragmentManager fm =getChildFragmentManager();
                    fm.beginTransaction().hide(lineMusic_state_1).commit();
                    fm.beginTransaction().add(R.id.line_Main_Fragment,lineMusic_state_2).show(lineMusic_state_2).commit();
                }else {
                SEARCH=1;
            }
            }
    }};
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void SetSearchText(MessageWord messageWord){
        if (!searchEdit.getText().equals(messageWord.getWord()))
        {
            searchEdit.setText( messageWord.getWord());
            SEARCH=2;

        }
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
}
