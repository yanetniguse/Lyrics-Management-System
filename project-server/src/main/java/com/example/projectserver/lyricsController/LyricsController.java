package com.example.projectserver.lyricsController;

import com.example.projectserver.exceptions.LyricsIdMismatchException;
import com.example.projectserver.lyricsModel.Lyrics;
import com.example.projectserver.lyricsModel.LyricsDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lyrics")
public class LyricsController {
    @Autowired
    private LyricsDAO lyricsDAO;

    @GetMapping("/")
    public List<Lyrics> defaultPath() {
        return lyricsDAO.getAllLyrics();
    }

    @GetMapping("/get-all")
    public List<Lyrics> getAllLyrics() {
        return lyricsDAO.getAllLyrics();
    }

    @PostMapping("/save")
    public Lyrics save(@RequestBody Lyrics lyrics) {
        return lyricsDAO.save(lyrics);
    }

    @GetMapping("/{id}")
    public List<Lyrics> getLyricsById(@PathVariable int id) {
        return lyricsDAO.getLyricsById(id);
    }

    @PutMapping("/update/{id}")
    public Lyrics updateLyrics(@RequestBody Lyrics lyrics, @PathVariable int id) {
        if (lyrics.getId() != id) {
            throw new LyricsIdMismatchException();
        }

        lyricsDAO.getLyricsById(id);

        return lyricsDAO.save(lyrics);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteLyrics(@PathVariable int id) {
        lyricsDAO.delete(id);
    }
}
