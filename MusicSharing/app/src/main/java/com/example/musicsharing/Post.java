package com.example.musicsharing;

public class Post {
    String songname;
    String album_artist;
    String caption;
    String image;

    String location;



    public Post(String songname, String album_artist, String caption, String image, String location) {
        this.songname = songname;
        this.album_artist = album_artist;
        this.caption = caption;
        this.image = image;
        this.location = location;
    }

    public String getSongname() {
        return songname;
    }

    public void setSongname(String songname) {
        this.songname = songname;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
