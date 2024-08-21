package com.example.projectserver.lyricsModel;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LyricsRepository extends CrudRepository<Lyrics, Integer> {
}
