package com.example.demo.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrackFromRedactor {

    private String name;

    private String path;

    private Long genreId;

    private Long authorId;

}
