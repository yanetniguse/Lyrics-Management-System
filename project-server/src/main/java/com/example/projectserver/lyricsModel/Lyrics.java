package com.example.projectserver.lyricsModel;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Lyrics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String songTitle;
    private String songLyrics;
    private String songArtist;

    protected Lyrics() {
    }

    public Lyrics(String songTitle, String songLyrics, String songArtist) {
        this.songTitle = songTitle;
        this.songLyrics = songLyrics;
        this.songArtist = songArtist;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getSongLyrics() {
        return songLyrics;
    }

    public void setSongLyrics(String songLyrics) {
        this.songLyrics = songLyrics;
    }

    public String getSongArtist() {
        return songArtist;
    }

    public void setSongArtist(String songArtist) {
        this.songArtist = songArtist;
    }

    @Override
    public String toString() {
        return "Lyrics{" +
                "id=" + id +
                ", songTitle='" + songTitle + '\'' +
                ", songLyrics='" + songLyrics + '\'' +
                ", songArtist='" + songArtist + '\'' +
                '}';
    }
}
