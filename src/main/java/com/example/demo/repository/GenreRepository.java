package com.example.demo.repository;

import com.example.demo.entity.Genre;
import org.springframework.data.repository.CrudRepository;

public interface GenreRepository extends CrudRepository<Genre,Long> {
    Genre findByName(String name);
}
