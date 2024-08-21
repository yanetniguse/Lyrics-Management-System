package com.example.projectserver;

import com.example.projectserver.lyricsModel.Lyrics;
import com.example.projectserver.lyricsModel.LyricsDAO;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = {"com.example.projectserver", "com.example.projectserver.config"})
@EnableAutoConfiguration
public class ProjectServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectServerApplication.class, args);
    }

    // Code that generates dummy data for testing, to be removed
    @Bean
    public CommandLineRunner demo(LyricsDAO lyrics) {
        return (args) -> {
            // Save 12 dummy songs
            lyrics.save(new Lyrics("Bundle", "Titanium", "Prompt"));
            lyrics.save(new Lyrics("Break Me", "Fill me up crazy", "Jaguar"));
            lyrics.save(new Lyrics("Before Us", "Still we rise", "Fabrizio"));
            lyrics.save(new Lyrics("Another Song", "Some more lyrics", "Artist A"));
            lyrics.save(new Lyrics("Yet Another Song", "Even more lyrics", "Artist B"));
            lyrics.save(new Lyrics("Awesome Tune", "Fantastic lyrics", "Amazing Artist"));
            lyrics.save(new Lyrics("Melody of Joy", "Happy lyrics", "Singer X"));
            lyrics.save(new Lyrics("Eternal Harmony", "Peaceful lyrics", "Vocalist Y"));
            lyrics.save(new Lyrics("Heartbeat Symphony", "Romantic lyrics", "Singer Z"));
            lyrics.save(new Lyrics("Dreamy Serenade", "Dreamy lyrics", "Dream Singer"));
            lyrics.save(new Lyrics("Rhythm of Life", "Energetic lyrics", "Groovy Artist"));
            lyrics.save(new Lyrics("Soulful Ballad", "Emotional lyrics", "Sentimental Singer"));
        };
    }
}
