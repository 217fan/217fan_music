package com.example.administrator.text1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.text1.R;
import com.example.administrator.text1.base.MyApplication;
import com.example.administrator.text1.bean.Music;

import java.util.ArrayList;
import java.util.List;

public class MusiclistAdapter extends BaseAdapter{
    List <Music>musicList;
    private final LayoutInflater mInflater;
    private final int mResource;
    private Context mcontext;
    MyApplication myApplication;
    boolean isdelect;
    boolean isFavorate;

    public MusiclistAdapter(Context context, int resId, List<Music> list){

        mcontext=context;
        mInflater= LayoutInflater.from(context);
        mResource=resId;
        musicList=list;
    }
    @Override
    public int getCount() {
        return musicList.size();
    }

    @Override
    public Object getItem(int i) {
        return musicList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Music musicitem=(Music)getItem(i);
        ViewHolder viewHolder;
        if (view==null){
            view=mInflater.inflate(mResource,viewGroup,false);
            viewHolder=new ViewHolder();
            viewHolder.data=(TextView)view.findViewById(R.id.data);
            viewHolder.isdelect=(ImageView) view.findViewById(R.id.isdelect);
            viewHolder.isFavorate=(ImageView)view.findViewById(R.id.isfavorite);
            view.setTag(viewHolder);
        }else {
            viewHolder=(ViewHolder) view.getTag();
        }

        viewHolder.data.setText(musicitem.name+""+musicitem.artist);
        return view;
    }
    class ViewHolder{
        TextView data;
        ImageView isdelect;
        ImageView isFavorate;
    }
}
