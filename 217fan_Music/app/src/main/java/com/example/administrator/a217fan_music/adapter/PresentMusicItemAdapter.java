package com.example.administrator.a217fan_music.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.a217fan_music.R;
import com.example.administrator.a217fan_music.bean.Music;

import java.util.List;

public class PresentMusicItemAdapter extends BaseAdapter {
    private List<Music> musicList;
    private LayoutInflater mInflater;
    private  int mResource;
    private Context mcontext;

    public PresentMusicItemAdapter(Context context, int resId, List<Music> list){

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
    public View getView(final int position, View convertView, ViewGroup parent) {
        Music musicitem=(Music)getItem(position);
       ViewHolder viewHolder;
        if(convertView ==null){
            convertView=mInflater.inflate(mResource,parent,false);
            viewHolder=new ViewHolder();
            viewHolder.musicitem_name=(TextView)convertView.findViewById(R.id.present_musicitem_songname);
            viewHolder.musicitem_artist=(TextView)convertView.findViewById(R.id.present_musicitem_songartist);
            viewHolder.musicitem_delect=(ImageView)convertView.findViewById(R.id.present_musicitem_delectmusic);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder=(ViewHolder)convertView.getTag();
        }
        viewHolder.musicitem_name.setText(" "+musicitem.name);
        viewHolder.musicitem_artist.setText(" "+musicitem.artist+"."+musicitem.albumname);
        viewHolder.musicitem_delect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monItemDelectListener.onDelectonＣlick(position);
            }
        });
        return convertView;
    }

    //定义删除接口
   public interface  onItemDelectListener{
            void onDelectonＣlick(int i);
    }
    private onItemDelectListener monItemDelectListener;
    public void setonItemDelectListener(onItemDelectListener monItemDelectListener){
        this.monItemDelectListener=monItemDelectListener;
    }

    class ViewHolder{
        TextView musicitem_name;
        TextView  musicitem_artist;
        ImageView musicitem_delect;
    }
}
