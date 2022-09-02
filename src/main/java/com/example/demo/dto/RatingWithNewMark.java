package com.example.demo.dto;

import com.example.demo.entity.Track;
import com.example.demo.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingWithNewMark {


    private int mark;

    private User user;

    private Track track;

}
