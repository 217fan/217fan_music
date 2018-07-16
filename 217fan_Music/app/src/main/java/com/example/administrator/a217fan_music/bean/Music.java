package com.example.administrator.a217fan_music.bean;

import android.graphics.Bitmap;
import android.net.Uri;

import org.litepal.crud.DataSupport;

import java.net.URL;

public class Music extends DataSupport {


    public String name;
    public String artist;
    public String Url;
    public String songUri;
    public String albumUri;
    public String albumname;
    public int playstate;
    public void setState(String state) {
        this.state = state;
    }
    public String line_songId;
    public String state;
    public long duration;
    public long playedtime;
    public boolean isfavorite;
    public String listname;
    public Bitmap bitmap;
    public String songSheet;
    public int getState() {
        return playstate;
    }

    public void setState(int state) {
        this.playstate = state;
    }

    public String getSongSheet() {
        return songSheet;
    }

    public void setSongSheet(String songSheet) {
        this.songSheet = songSheet;
    }


    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getAlbumname() {
        return albumname;
    }

    public void setAlbumname(String albumname) {
        this.albumname = albumname;
    }

    public void setListname(String listname) {
        this.listname = listname;
    }



    public Bitmap getBitmap() {
        return bitmap; }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getListname(){return listname;}

    public void setListname(){this.listname=listname;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getSongUri() {
        return songUri;
    }

    public void setSongUri(String songUri) {
        this.songUri = songUri;
    }

    public String getAlbumUri() {
        return albumUri;
    }

    public void setAlbumUri(String albumUri) {
        this.albumUri = albumUri;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getPlayedtime() {
        return playedtime;
    }

    public void setPlayedtime(long playedtime) {
        this.playedtime = playedtime;
    }

    public boolean isIsfavorite() {
        return isfavorite;
    }

    public void setIsfavorite(boolean isfavorite) {
        this.isfavorite = isfavorite;
    }

    public Music(String name, String artist, String songUri, String albumUri, long duration, long playedtime, boolean isfavorite){

        this.albumUri=albumUri;
        this.artist=artist;
        this.duration=duration;
        this.isfavorite=isfavorite;
        this.songUri=songUri;
        this.name=name;
        this.playedtime=playedtime;
    }
    public Music(){

    }
}
