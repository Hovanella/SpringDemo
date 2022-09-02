package com.example.demo.dto;

import com.example.demo.entity.Author;
import com.example.demo.entity.Genre;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrackDto {

    private Long id;

    private String name;

    private String path;

    private Genre genre;

    private Author author;

    private int rating = -1;

}
