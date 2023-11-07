package com.example.musicsharing;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Data {

    @SerializedName("data")
    private ArrayList<Song> songs = new ArrayList<Song>();

    public ArrayList<Song> getSongs() {return songs;}
    public class Song{
        private String title;

        @SerializedName("album")
        private Album mAlbum;

        @SerializedName("artist")
        private Artist mArtist;

        public String getTitle() {
            return title;
        }

        public Artist getArtist() {
            return mArtist;
        }

        public Album getAlbum() {return mAlbum;}

        public class Artist{
            private String name;

            public String getName() {
                return name;
            }
        }

        public class Album{
            @SerializedName("cover_medium")
            private String cover;

            public String getCover() {return cover;}
        }

    }
}
