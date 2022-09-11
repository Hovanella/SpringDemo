package com.example.demo.controller;

import com.example.demo.dto.AuthorDto;
import com.example.demo.dto.AuthorFromServer;
import com.example.demo.entity.Author;
import com.example.demo.service.Interfaces.AuthorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/authors/")
@CrossOrigin("*")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }


    @PostMapping(value = "AddAuthor",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Author> addAuthor(@RequestBody AuthorDto authorDto) {

        var author = authorService.CreateAuthor(authorDto);
        return new ResponseEntity<>(author, HttpStatus.OK);
    }

    @GetMapping(value = "GetAuthor/{name}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Author> getAuthor(@PathVariable String name) {

        var author = authorService.getAuthorByName(name);
        return new ResponseEntity<>(author, HttpStatus.OK);
    }

    @GetMapping(value="GetAllAuthors",produces = MediaType.APPLICATION_JSON_VALUE)
public ResponseEntity<Collection<AuthorFromServer>> getAllAuthors() {

        var authors = authorService.getAllAuthors();
        return new ResponseEntity<>(authors, HttpStatus.OK);
    }

}
