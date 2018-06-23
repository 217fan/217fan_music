package com.example.administrator.text1.bean;

import android.graphics.Bitmap;
import android.net.Uri;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.security.PublicKey;
import java.util.Date;

public class Music extends DataSupport {

    public String id;
   public String name;
   public String artist;
   public String Url;
   public Uri songUri;
   public Uri albumUri;
   public String albumname;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public long duration;
    public long playedtime;
    public boolean isfavorite;
    public String listname;
    public Bitmap bitmap;

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

    public Uri getSongUri() {
        return songUri;
    }

    public void setSongUri(Uri songUri) {
        this.songUri = songUri;
    }

    public Uri getAlbumUri() {
        return albumUri;
    }

    public void setAlbumUri(Uri albumUri) {
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

    public Music(String name, String artist, Uri songUri, Uri albumUri, long duration, long playedtime, boolean isfavorite){

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
