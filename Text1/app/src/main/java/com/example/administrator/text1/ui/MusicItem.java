package com.example.administrator.text1.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.text1.R;

public class MusicItem extends LinearLayout {

    public MusicItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.musicitem,this);
        TextView musicitem_name=(TextView)findViewById(R.id.musicitem_name);
        TextView musicitem_artist=(TextView)findViewById(R.id.musicitem_artist);

    }
}
