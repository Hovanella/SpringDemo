package com.example.demo.service.Interfaces;

import com.example.demo.dto.AuthorDto;
import com.example.demo.entity.Author;

public interface AuthorService {

    Author CreateAuthor(AuthorDto authorDto);
    Author getAuthorById(Long authorId);
}
