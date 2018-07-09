package com.example.administrator.a217fan_music.utils;

public class MessageEvent {
    private  boolean isplaying;
    private  String songname;
    private String songartist;

    public long getPlayedTime() {
        return playedTime;
    }

    public void setPlayedTime(long playedTime) {
        this.playedTime = playedTime;
    }

    private long playedTime;
    public boolean getIsplaying() {
        return isplaying;
    }

    public void setIsplaying(boolean isplaying) {
        this.isplaying = isplaying;
    }

    public String getSongname() {
        return songname;
    }

    public void setSongname(String songname) {
        this.songname = songname;
    }

    public String getSongartist() {
        return songartist;
    }

    public void setSongartist(String songartist) {
        this.songartist = songartist;
    }


}
