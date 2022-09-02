package com.example.demo.service;

import com.example.demo.dto.GenreDto;
import com.example.demo.entity.Genre;
import com.example.demo.repository.GenreRepository;
import com.example.demo.service.Interfaces.GenreService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GenreServiceImpl implements GenreService {

    private final ModelMapper modelMapper;
    private final GenreRepository genreRepository;

    @Autowired
    public GenreServiceImpl(ModelMapper modelMapper, GenreRepository genreRepository) {
        this.modelMapper = modelMapper;
        this.genreRepository = genreRepository;
    }

    @Override
    public Genre createGenre(GenreDto genreDto) {
        var genre = this.modelMapper.map(genreDto, Genre.class);
        return genreRepository.save(genre);
    }

    @Override
    public Genre findGenreByName(String name) {
        return genreRepository.findByName(name);
    }
}
