package com.chokealot.genreservice.repository;


import com.chokealot.genreservice.models.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre, Integer> {

    Optional<Genre> findGenreByGenre(String search);

}
