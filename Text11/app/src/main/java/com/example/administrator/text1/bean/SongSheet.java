package com.example.administrator.text1.bean;

import java.util.ArrayList;
import java.util.List;

public class SongSheet {
    List<Music>musicList;
    String name;

    public SongSheet(String name, List<Music> musicList){
        this.musicList=musicList;
        this.name=name;
    }
}
