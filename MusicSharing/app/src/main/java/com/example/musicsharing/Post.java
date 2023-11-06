package com.example.musicsharing;

public class Post {
    String songname;
    String album_artist;
    String caption;
    int image;


    public Post(String songname, String album_artist, String caption, int image) {
        this.songname = songname;
        this.album_artist = album_artist;
        this.caption = caption;
        this.image = image;
    }

    public String getSongname() {
        return songname;
    }

    public void setSongname(String songname) {
        this.songname = songname;
    }

    public String getAlbum_artist() {
        return album_artist;
    }

    public void setAlbum_artist(String album_artist) {
        this.album_artist = album_artist;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
