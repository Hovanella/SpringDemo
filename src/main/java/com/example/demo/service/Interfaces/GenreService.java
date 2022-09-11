package com.example.demo.service.Interfaces;

import com.example.demo.dto.GenreDto;
import com.example.demo.dto.GenreFromServer;
import com.example.demo.entity.Genre;

import java.util.Collection;

public interface GenreService {

    Genre createGenre(GenreDto genreDto);

    Genre findGenreByName(String name);

    Collection<GenreFromServer> getAllGenres();

    Genre getGenreById(Long genreId);
}
