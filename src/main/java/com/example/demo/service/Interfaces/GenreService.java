package com.example.demo.service.Interfaces;

import com.example.demo.dto.GenreDto;
import com.example.demo.entity.Genre;

public interface GenreService {

    Genre createGenre(GenreDto genreDto);

    Genre findGenreByName(String name);

    Genre getGenreById(Long genreId);
}
