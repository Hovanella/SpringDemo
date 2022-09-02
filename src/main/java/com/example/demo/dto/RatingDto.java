package com.example.demo.dto;

import com.example.demo.entity.Track;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingDto {

    private int mark;

    private Track track;
}
