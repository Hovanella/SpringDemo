package com.example.demo.repository;

import com.example.demo.entity.Rating;
import com.example.demo.entity.Track;
import com.example.demo.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface RatingRepository extends CrudRepository<Rating, Long> {

    Collection<Rating> findByUser(User user);

    Rating findByUserAndTrack(User user, Track track);
}
