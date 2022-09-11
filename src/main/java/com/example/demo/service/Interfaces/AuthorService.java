package com.example.demo.service.Interfaces;

import com.example.demo.dto.AuthorDto;
import com.example.demo.dto.AuthorFromServer;
import com.example.demo.entity.Author;

import java.util.Collection;

public interface AuthorService {

    Author CreateAuthor(AuthorDto authorDto);
    Author getAuthorById(Long authorId);
    Author getAuthorByName(String name);
    Collection<AuthorFromServer> getAllAuthors();
}
