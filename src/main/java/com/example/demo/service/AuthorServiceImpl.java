package com.example.demo.service;

import com.example.demo.dto.AuthorDto;
import com.example.demo.dto.AuthorFromServer;
import com.example.demo.entity.Author;
import com.example.demo.repository.AuthorRepository;
import com.example.demo.service.Interfaces.AuthorService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository, ModelMapper modelMapper) {
        this.authorRepository = authorRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public Author CreateAuthor(AuthorDto authorName) {
        var author = this.modelMapper.map(authorName, Author.class);
        return authorRepository.save(author);
    }

    @Override
    public Author getAuthorById(Long authorId) {
        return authorRepository.findById(authorId).orElse(null);
    }

    @Override
    public Author getAuthorByName(String name) {
        return authorRepository.findAuthorByName(name);
    }

    @Override
    public Collection<AuthorFromServer> getAllAuthors() {
        var authors = authorRepository.findAll();

        var authorsFromServer = new ArrayList<AuthorFromServer>();

        for (var author : authors) {
            var authorFromServer = this.modelMapper.map(author, AuthorFromServer.class);
            authorsFromServer.add(authorFromServer);
        }

        return authorsFromServer;

    }






}
