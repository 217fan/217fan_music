package com.example.administrator.text1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.text1.R;
import com.example.administrator.text1.bean.Music;

import java.util.List;

public class MusicAdapter extends BaseAdapter {
    private List<Music>musicList;
    private final LayoutInflater mInflater;
    private final int mResource;
    private Context mcontext;

    public MusicAdapter(Context context, int resId, List<Music> list){

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
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Music musicitem=(Music)getItem(i);
        ViewHolder viewHolder;
        if(view ==null){
            view=mInflater.inflate(mResource,viewGroup,false);
            viewHolder=new ViewHolder();
            viewHolder.musicitem_name=(TextView)view.findViewById(R.id.musicitem_name);
            viewHolder.musicitem_artist=(TextView)view.findViewById(R.id.musicitem_artist);
            view.setTag(viewHolder);
        }
        else {
            viewHolder=(ViewHolder)view.getTag();
        }
        viewHolder.musicitem_name.setText(musicitem.name);
        viewHolder.musicitem_artist.setText(musicitem.artist);
        return view;
    }
    class ViewHolder{
        TextView musicitem_name;
        TextView musicitem_artist;
    }
}
