package com.example.demo.controller;
import com.example.demo.dto.AuthorDto;
import com.example.demo.entity.Author;
import com.example.demo.service.Interfaces.AuthorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


}
