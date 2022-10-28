package com.example.demo.controller;


import com.example.demo.dto.RatingForUpdate;
import com.example.demo.dto.TrackDto;
import com.example.demo.dto.TrackFromRedactor;
import com.example.demo.service.AuthorServiceImpl;
import com.example.demo.service.GenreServiceImpl;
import com.example.demo.service.TrackServiceImpl;
import com.example.demo.service.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;

@RestController
@RequestMapping("/tracks/")
@CrossOrigin("*")
@Slf4j
public class TrackController {

    private final TrackServiceImpl trackService;
    private final UserServiceImpl userService;
    private final GenreServiceImpl genreService;
    private final AuthorServiceImpl authorService;

    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    public TrackController(TrackServiceImpl trackService, UserServiceImpl userService, GenreServiceImpl genreService, AuthorServiceImpl authorService) {
        this.trackService = trackService;
        this.userService = userService;
        this.genreService = genreService;
        this.authorService = authorService;
    }

    @PostMapping(value = "AddTrack", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TrackDto> addTrack(@RequestBody TrackFromRedactor trackFromRedactor) {

        var author = authorService.getAuthorById(trackFromRedactor.getAuthorId());
        var genre = genreService.getGenreById(trackFromRedactor.getGenreId());
        var name = trackFromRedactor.getName();
        var path = trackFromRedactor.getPath();

        var trackDto = trackService.createTrack(name, path,author,genre);

        return new ResponseEntity<>(trackDto, HttpStatus.OK);

    }

    @PostMapping(value = "CopyTrackInAudio", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity uploadFile(@RequestBody MultipartFile file) {

        //save file in directory
        try {
            var inputStream = file.getInputStream();
            var path = file.getOriginalFilename();
            var destination = new File("E:\\Education\\5semester\\Spring\\MyLabs\\Demo\\demo\\src\\main\\resources\\static\\react-js-example\\public\\Audio\\" + path).toPath();

            Files.copy(inputStream, destination);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok().build();
    }


    @GetMapping(value = "TracksForUser/{login}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<TrackDto>> getUserRatedTracks(@PathVariable String login) {

        Long id = userService.getUserIdByLogin(login);

        if (id == null)
            return ResponseEntity.badRequest().build();

        var tracks = trackService.getAllTracksForCurrentUser(id);

        return new ResponseEntity<>(tracks, HttpStatus.OK);

    }

    @PostMapping(value = "AddOrUpdateRating",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TrackDto> addOrUpdateTrackUserRating(@RequestBody RatingForUpdate ratingForUpdate) {

        var login = ratingForUpdate.getLogin();
        var trackId = ratingForUpdate.getTrackId();
        var mark = ratingForUpdate.getMark();

        Long id = userService.getUserIdByLogin(login);
        if (id == null)
            return ResponseEntity.badRequest().build();

        var track = trackService.addOrUpdateTrackUserRating(trackId, mark, id);
        return new ResponseEntity<>(track, HttpStatus.OK);
    }


}


