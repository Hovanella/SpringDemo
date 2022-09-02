package com.example.demo.repository;

import com.example.demo.entity.Track;
import org.springframework.data.repository.CrudRepository;

public interface TrackRepository extends CrudRepository<Track, Long> {

}
