package com.example.administrator.text1.activity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.JsonReader;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.administrator.text1.R;
import com.example.administrator.text1.bean.Music;
import com.example.administrator.text1.fragment.SearchFragment1;
import com.example.administrator.text1.fragment.SearchFragment3;
import com.example.administrator.text1.utils.Http;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SearchMusic extends AppCompatActivity  {
    private static final String TAG = "SearchMusic";
   private  static int SEARCH=1;
   //private final static int INTERVAL=300;

    FragmentManager fragmentManager;
    SearchFragment1 fragment1;
    SearchFragment3 fragment3;
    EditText editText;
    Http http;
    MediaPlayer mediaPlayer;
    static String word="";
    static  int state=1;
    static int editchange=1;
    ListView listView;
    List<String>simplelist=new ArrayList<String>();
    ArrayAdapter<String>simpleAdapter;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        editText=(EditText)findViewById(R.id.search);
        fragmentManager=getSupportFragmentManager();
        fragment1=new SearchFragment1();
        fragment3=new SearchFragment3();
        fragmentManager.beginTransaction().add(R.id.frame,fragment1).commit();
        fragmentManager.beginTransaction().add(R.id.frame,fragment3).commit();
        listView=(ListView)findViewById(R.id.list_search);
        simpleAdapter=new ArrayAdapter<String>(SearchMusic.this,android.R.layout.simple_list_item_1,simplelist);
        fragmentManager.beginTransaction().show(fragment1).commit();
        fragmentManager.beginTransaction().hide(fragment3).commit();
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String ss=charSequence.toString().replace(" ","");

            }

            @Override
            public void afterTextChanged(Editable editable) {

                    if (searchHandler.hasMessages(SEARCH)){
                        searchHandler.removeMessages(SEARCH);
                    }
                    searchHandler.sendEmptyMessageDelayed(SEARCH,300);
                  /*  editchange=1;
                    fragmentManager.beginTransaction().hide(fragment1).commit();
                    fragmentManager.beginTransaction().hide(fragment3).commit();
                    state=2;
                     simplelist.clear();
                    if(simpleAdapter!=null){
                    simpleAdapter.notifyDataSetChanged();
                 }
              //  Http.list.clear();
                Log.w(TAG, "afterTextChanged: Httplist"+Http.list.size() );
                if (editText.getText()!=null){
                    Http.list.clear();
                    word=editText.getText().toString();
                    getid();
                    int i=0;
                    Log.w(TAG, "afterTextChanged:Http.isfinish "+Http.isFinishid );
                    while (Http.isFinishid!=true){
                        i++;

                    }
                    Log.w(TAG, "afterTextChanged:Http.isfinish "+Http.isFinishid );
                    for (int i1=0;i1<Http.list.size();i1++){
                        simplelist.add(Http.list.get(i1).name);
                    }
                    simpleAdapter.notifyDataSetChanged();
                    Log.w(TAG, "afterTextChanged: "+simplelist.size() );
                    listView.setAdapter(simpleAdapter);
                    Log.w(TAG, "afterTextChanged: 监听成功" );
                }*/
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Log.w(TAG, "onItemClick: "+ simplelist.size());
                Http.music=Http.list.get(i);
                    SEARCH=2;
                    editText.setText(Http.music.name);
                    simplelist.clear();
                    simpleAdapter.notifyDataSetChanged();
                word=Http.music.name;
                editText.setSelection(word.length());
                state=3;
                Log.w(TAG, "onItemClick:关键字是 "+word);
                getid();
                while (Http.isFinishid==false){
                    i++;
                }
                    Log.w(TAG, "onItemClick: 只执行show " );
                    fragmentManager.beginTransaction().show(fragment3).commit();

            }
        });
    }

    void getid(){
        Http.list.clear();
        Http http=new Http();
        http.getid_Json(word, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.w(TAG, "onFailure: 失败了" );
                Log.w(TAG, "onFailure: "+e.toString() );
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {

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
            http.getSongUrl_Json(Http.music.id, new Callback() {
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
                music.id=jsonObject2.getString("id");
                music.artist=jsonObjectartist.getString("name");
                music.albumname=jsonObjectalbum.getString("name");
                Log.w(TAG, "parsegetJsonid:id "+music.id);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
    private Handler searchHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
           if(msg.what==1){
               editchange=1;
               fragmentManager.beginTransaction().hide(fragment1).commit();
               fragmentManager.beginTransaction().hide(fragment3).commit();
               state=2;
               simplelist.clear();
                   simpleAdapter.notifyDataSetChanged();
               //  Http.list.clear();
               Log.w(TAG, "afterTextChanged: Httplist"+Http.list.size() );
               if (editText.getText()!=null){
                   Http.list.clear();
                   word=editText.getText().toString();
                   getid();
                   int i=0;
                   Log.w(TAG, "afterTextChanged:Http.isfinish "+Http.isFinishid );
                   while (Http.isFinishid!=true){
                       i++;
                   }
                   Log.w(TAG, "afterTextChanged:Http.isfinish "+Http.isFinishid );
                   for (int i1=0;i1<Http.list.size();i1++){
                       simplelist.add(Http.list.get(i1).name);
                   }
                   simpleAdapter.notifyDataSetChanged();
                   Log.w(TAG, "afterTextChanged: "+simplelist.size() );
                   listView.setAdapter(simpleAdapter);
                   Log.w(TAG, "afterTextChanged: 监听成功" );
               }
           }else {
               SEARCH=1;
               }
        }
    };
}
