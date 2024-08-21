package com.example.projectserver.lyricsModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class LyricsDAO {
    @Autowired
    private LyricsRepository lyricsRepository;

    public Lyrics save(Lyrics lyrics) {
        return lyricsRepository.save(lyrics);
    }

    public List<Lyrics> getAllLyrics() {
        List<Lyrics> lyricsList = new ArrayList<>();

        Streamable.of(lyricsRepository.findAll()).forEach(lyricsList::add);

        return lyricsList;
    }

    public List<Lyrics> getLyricsById(int id) {
        List<Lyrics> lyricsList = new ArrayList<>();

        Streamable.of(lyricsRepository.findAllById(Collections.singleton(id))).forEach(lyricsList::add);

        return lyricsList;
    }

    public void delete(int lyricsId) {
        lyricsRepository.deleteById(lyricsId);
    }
}
