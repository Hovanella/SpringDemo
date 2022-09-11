package com.example.demo.controller;

import com.example.demo.dto.GenreDto;
import com.example.demo.dto.GenreFromServer;
import com.example.demo.entity.Genre;
import com.example.demo.service.Interfaces.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/genres/")
@CrossOrigin("*")
public class GenreController {

    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }


    @PostMapping(value = "AddGenre", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Genre> addAuthor(@RequestBody GenreDto genreDto) {


        if (genreService.findGenreByName(genreDto.getName()) == null) {
            var genre = genreService.createGenre(genreDto);
            return new ResponseEntity<>(genre, HttpStatus.OK);
        } else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "GetGenre/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Genre> getGenre(@PathVariable String name) {

        var genre = genreService.findGenreByName(name);
        return new ResponseEntity<>(genre, HttpStatus.OK);
    }



    @GetMapping(value="GetAllGenres",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<GenreFromServer>> getAllGenres() {

        var genres = genreService.getAllGenres();
        return new ResponseEntity<>(genres, HttpStatus.OK);
    }

}
